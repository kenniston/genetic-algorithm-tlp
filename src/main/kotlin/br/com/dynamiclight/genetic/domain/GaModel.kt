package br.com.dynamiclight.genetic.domain

import tornadofx.*

enum class MutationType {
    INDIVIDUAL, GENERAL, GENES
}

data class Point(val x: Double, val y: Double, val radius: Double, val color: String)

class GaModel {
    var population: Int by property(0)
    fun populationProperty() = getProperty(GaModel::population)

    var crossoverRate: Double by property(0.0)
    fun crossoverRateProperty() = getProperty(GaModel::crossoverRate)

    var mutationRate: Double by property(0.0)
    fun mutationRateProperty() = getProperty(GaModel::mutationRate)

    var evolutions: Int by property(0)
    fun evolutionsProperty() = getProperty(GaModel::evolutions)

    var mutationType: MutationType by property(MutationType.INDIVIDUAL)
    fun mutationTypeProperty() = getProperty(GaModel::mutationType)

    var useElitism: Boolean by property(false)
    fun useElitismProperty() = getProperty(GaModel::useElitism)

    var elitismCount: Int by property(0)
    fun elitismCountProperty() = getProperty(GaModel::elitismCount)

    var tournament: Int by property(0)
    fun tournamentProperty() = getProperty(GaModel::tournament)

    val points = mutableListOf<Point>()
    val pointsDistance = mutableMapOf<String, Double>()
}