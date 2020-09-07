package br.com.dynamiclight.genetic.viewmodel

import br.com.dynamiclight.genetic.domain.GaModel
import br.com.dynamiclight.genetic.interactor.GaInteractor
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.shape.Circle
import tornadofx.*
import java.io.File

class GaViewModel : ItemViewModel<GaModel>() {
    private val interactor: GaInteractor by inject()

    val population = bind(GaModel::populationProperty)
    val crossoverRate = bind(GaModel::crossoverRateProperty) as DoubleProperty
    val mutationRate = bind(GaModel::mutationRateProperty) as DoubleProperty
    val evolutions = bind(GaModel::evolutionsProperty)
    val mutationType = bind(GaModel::mutationTypeProperty) as SimpleObjectProperty
    val useElitism = bind(GaModel::useElitismProperty)
    val elitismCount = bind(GaModel::elitismCountProperty)
    val tournament = bind(GaModel::tournamentProperty)

    init {
        item = interactor.model
    }

    override fun onCommit() {

    }

    fun addPoint(point: Circle) {
        interactor.addPoint(point.centerX, point.centerY, point.radius, point.fill.toString())
    }

    fun save(file: File?) {
        file?.let { interactor.save(it) }
    }

    fun load(file: File?) {
        file?.let { interactor.load(it) }
    }

}