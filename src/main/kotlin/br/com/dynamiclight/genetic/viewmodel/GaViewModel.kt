package br.com.dynamiclight.genetic.viewmodel

import br.com.dynamiclight.genetic.domain.GAResult
import br.com.dynamiclight.genetic.domain.GaModel
import br.com.dynamiclight.genetic.interactor.GaInteractor
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
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

    val statusProperty = SimpleStringProperty("")
    var status: String by statusProperty

    val evolutionResultProperty = SimpleStringProperty(messages["evolutions.result.label"])
    var evolutionResult: String by evolutionResultProperty

    val shortestDistanceProperty = SimpleStringProperty(messages["shortest.distance.label"])
    var shortestDistance: String by shortestDistanceProperty

    init {
        item = interactor.model
    }

    override fun onCommit() {

    }

    fun addCity(city: Circle) {
        interactor.addPoint(city.centerX, city.centerY, city.radius, city.fill.toString())
    }

    fun createPopulation() : GAResult<Unit> {
        commit()
        return interactor.createPopulation()
    }

    fun save(file: File?) : GAResult<Unit> {
        return if (file != null) {
            commit()
            interactor.save(file)
        } else GAResult.Canceled()
    }

    fun load(file: File?) : GAResult<Unit> {
        return if (file != null) {
            when (val result = interactor.load(file)) {
                is GAResult.Success -> {
                    item = result.data
                    GAResult.Success(Unit)
                }
                is GAResult.Error -> GAResult.Error(result.error)
                else -> GAResult.Canceled()
            }
        } else GAResult.Canceled()
    }

}