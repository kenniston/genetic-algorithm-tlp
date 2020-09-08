package br.com.dynamiclight.genetic.interactor

import br.com.dynamiclight.genetic.domain.GaModel
import br.com.dynamiclight.genetic.domain.City
import br.com.dynamiclight.genetic.domain.GAResult
import br.com.dynamiclight.genetic.domain.Individual
import br.com.dynamiclight.genetic.repository.GaRepository
import tornadofx.*
import java.io.File
import java.lang.Exception
import kotlin.math.pow
import kotlin.math.sqrt

class GaInteractor : Component(), ScopedInstance {
    private val repository: GaRepository by inject()

    private var data = GaModel()
    val model: GaModel
        get() = data

    fun addPoint(x: Double, y: Double, radius: Double, color: String) {
        data.cities.add(City(x, y, radius, color))
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

    fun createPopulation() : GAResult<Unit> {
        if (data.cities.size < 2) return GAResult.Error(Exception(messages["error.city.quantity"]))

        if (data.population < 1) return GAResult.Error(Exception(messages["error.population.size"]))

        data.individuals.clear()
        for (index in 0 until data.population) {
            val chromosome = (0 until data.cities.size).shuffled()
            val fitness = calculateIndividualFitness(chromosome)
            val individual = Individual(chromosome, fitness)
            data.individuals.add(individual)
        }
        return GAResult.Success(Unit)
    }

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

    fun getCitiesDistance(start: Int, end: Int) : Double {
        val hash = "${data.cities[start].hashCode()}.${data.cities[end].hashCode()}"
        return data.citiesDistance[hash] ?: 0.0
    }

    fun save(file: File) : GAResult<Unit> {
        return repository.save(file, data)
    }

    fun load(file: File) : GAResult<GaModel> {
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