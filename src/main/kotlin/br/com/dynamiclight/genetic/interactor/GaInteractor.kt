package br.com.dynamiclight.genetic.interactor

import br.com.dynamiclight.genetic.domain.GaModel
import br.com.dynamiclight.genetic.domain.City
import br.com.dynamiclight.genetic.domain.GAResult
import br.com.dynamiclight.genetic.domain.Individual
import br.com.dynamiclight.genetic.repository.GaRepository
import io.github.serpro69.kfaker.Faker
import tornadofx.*
import java.io.File
import java.lang.Exception
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class GaInteractor : Component(), ScopedInstance {
    private val repository: GaRepository by inject()
    private val faker = Faker()

    private var data = GaModel()
    val model: GaModel
        get() = data

    // Cities

    fun addCity(name: String, x: Double, y: Double, radius: Double, color: String) {
        data.cities.add(City(name, x, y, radius, color))
        generateDistanceData()
    }

    private fun generateDistanceData() {
        data.citiesDistance.clear()
        data.cities.forEach { first ->
            data.cities.filter { it != first }.forEach { second ->
                data.citiesDistance["${first.hashCode()}.${second.hashCode()}"] =
                        sqrt((second.x - first.x).pow(2.0) + (second.y - first.y).pow(2.0))
            }
        }
    }

    fun getCitiesDistance(start: Int, end: Int): Double {
        val hash = "${data.cities[start].hashCode()}.${data.cities[end].hashCode()}"
        return data.citiesDistance[hash] ?: 0.0
    }

    // Individual

    private fun calculateIndividualFitness(chromosome: List<Int>): Double {
        var distance = 0.0
        chromosome.forEachIndexed { index, value ->
            distance += if (index < data.cities.size - 1) {
                getCitiesDistance(value, chromosome[index + 1])
            } else {
                getCitiesDistance(value, chromosome[0])
            }
        }
        return distance
    }

    // Population

    fun createPopulation(): GAResult<Unit> {
        if (data.cities.size < 2) return GAResult.Error(Exception(messages["error.city.quantity"]))
        if (data.population < 1) return GAResult.Error(Exception(messages["error.population.size"]))

        data.individuals.clear()
        for (index in 0 until data.population) {
            val chromosome = (0 until data.cities.size).shuffled().toMutableList()
            val fitness = calculateIndividualFitness(chromosome)
            val individual = Individual(faker.name.firstName(), chromosome, fitness, index)
            data.individuals.add(individual)
        }
        return GAResult.Success(Unit)
    }

    private fun updateIndividualPosition() {
        data.individuals.forEachIndexed { index, ind -> ind.position = index }
    }

    private fun populationFitnessAverage(): Double {
        var sum: Double = 0.0;
        data.individuals.forEach { sum += it.fitness }
        return sum / data.individuals.size
    }

    private fun sortPopulation() {
        data.individuals.sortBy { it.fitness }
        updateIndividualPosition()
    }

    private fun getWorstIndividual(): Individual {
        sortPopulation()
        return data.individuals.last()
    }

    private fun getBestIndividual(): Individual {
        sortPopulation()
        return data.individuals.first()
    }

    // Genetic Algorithm

    /**
     * Performs a new evolution in the Genetic Algorithm.
     * Steps:
     *      Create population
     *      Evaluate population
     *      Elitism
     *      Select parents (Tournament)
     *      Crossver
     *      Mutation
     *      Evaluation
     *      Insert new indiviuals into the new population
     */
    fun executeGA(): GAResult<Unit> {
        if (data.cities.size < 2) return GAResult.Error(Exception(messages["error.city.quantity"]))
        if (data.population < 1) return GAResult.Error(Exception(messages["error.population.size"]))

        // Crossover
        //println("Parent 1: ${data.individuals[0]}")
        //println("Parent 2: ${data.individuals[1]}")
        val (a, b) = crossoverPMX(data.individuals[0], data.individuals[1])
        //println("Child  1: $a")
        //println("Child  2: $b")

        // Mutation
        val m = mutation(b)
        println("Mutation: $m")

        // Tournament
        val tournamentResult = tournament()
        when (tournamentResult) {
            is GAResult.Success -> println("Winner: ${tournamentResult.data}")
            is GAResult.Error -> return tournamentResult
            else -> GAResult.Canceled()
        }

        return GAResult.Success(Unit)
    }

    private fun crossoverPMX(father1: Individual, father2: Individual): Pair<Individual, Individual> {
        val chromosomeSize = father1.chromosome.size
        val childChromosome1 = father1.chromosome.toMutableList()
        val childChromosome2 = father2.chromosome.toMutableList()
        val replacement1 = mutableMapOf<Int, Int>()
        val replacement2 = mutableMapOf<Int, Int>()

        // Select genes range
        val rnd = Random(System.currentTimeMillis())
        var start = rnd.nextInt(0, chromosomeSize)
        var end = rnd.nextInt(0, chromosomeSize)

        while (start == end) end = rnd.nextInt(0, chromosomeSize)
        if (start > end) start = end.also { end = start }

        // Crossover
        for (index in start..end) {
            replacement1[childChromosome1[index]] = childChromosome2[index]
            replacement2[childChromosome2[index]] = childChromosome1[index]
            childChromosome1[index] = childChromosome2[index].also { childChromosome2[index] = childChromosome1[index] }
        }

        // Remove duplicates
        for (index in 0 until chromosomeSize) {
            if (index in start..end) continue
            childChromosome1[index] = replacement2[childChromosome1[index]] ?: childChromosome1[index]
            childChromosome2[index] = replacement1[childChromosome2[index]] ?: childChromosome2[index]
        }

        val fitness1 = calculateIndividualFitness(childChromosome1)
        val individual1 = Individual(faker.name.firstName(), childChromosome1, fitness1, 0)

        val fitness2 = calculateIndividualFitness(childChromosome2)
        val individual2 = Individual(faker.name.firstName(), childChromosome2, fitness2, 0)

        return Pair(individual1, individual2)
    }

    private fun mutation(individual: Individual): Individual {
        val rnd = Random(System.currentTimeMillis())

        if (rnd.nextDouble(0.0, 1.0) > data.mutationRate ) return individual

        var chromosomeSize = individual.chromosome.size
        var start = rnd.nextInt(0, chromosomeSize)
        var end = rnd.nextInt(0, chromosomeSize)

        while (start == end) end = rnd.nextInt(0, chromosomeSize)

        individual.chromosome.swap(indexOne = start, indexTwo = end)
        individual.fitness = calculateIndividualFitness(individual.chromosome)

        return individual
    }

    private fun populationMutation() {
        data.individuals.forEach { mutation(it) }
    }

    private fun tournament(): GAResult<Individual> {
        if (data.tournament > data.individuals.size) return GAResult.Error(Exception(messages["error.tournament.size"]))
        if (data.tournament < 2) return GAResult.Error(Exception(messages["error.tournament.minimum.size"]))

        var winner = Individual(faker.name.firstName(), mutableListOf(), Double.MAX_VALUE, -1)

        var rnd = (0 until data.individuals.size).shuffled()
        for (index in 0 until data.tournament) {
            val competitor = data.individuals[rnd[index]]

            println("\tCompetidor: $competitor")

            winner = if (competitor.fitness < winner.fitness) competitor else winner
            winner.fitness = calculateIndividualFitness(winner.chromosome)
        }
        return GAResult.Success(winner)
    }

    // File

    fun save(file: File): GAResult<Unit> {
        return repository.save(file, data)
    }

    fun load(file: File): GAResult<GaModel> {
        return when (val result = repository.load(file)) {
            is GAResult.Success -> {
                data = result.data
                generateDistanceData()
                GAResult.Success(data)
            }
            else -> return GAResult.Error(Exception(messages["error.loading.model"]))
        }
    }

}
