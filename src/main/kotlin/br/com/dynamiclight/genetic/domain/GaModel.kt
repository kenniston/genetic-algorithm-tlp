package br.com.dynamiclight.genetic.domain

import javafx.scene.shape.Circle
import tornadofx.*

enum class MutationType {
    INDIVIDUAL, GENERAL, GENES
}

class GaModel {
    var population by property(0)
    fun populationProperty() = getProperty(GaModel::population)

    var crossoverRate by property(0.0)
    fun crossoverRateProperty() = getProperty(GaModel::crossoverRate)

    var mutationRate by property(0.0)
    fun mutationRateProperty() = getProperty(GaModel::mutationRate)

    var evolutions by property(0)
    fun evolutionsProperty() = getProperty(GaModel::evolutions)

    var mutationType by property(MutationType.INDIVIDUAL)
    fun mutationTypeProperty() = getProperty(GaModel::mutationType)

    var useElitism by property(false)
    fun useElitismProperty() = getProperty(GaModel::useElitism)

    var elitismCount by property(0)
    fun elitismCountProperty() = getProperty(GaModel::elitismCount)

    var tournament by property(0)
    fun tournamentProperty() = getProperty(GaModel::tournament)

    val points = mutableListOf<Circle>()
}