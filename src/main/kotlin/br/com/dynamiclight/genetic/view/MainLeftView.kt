package br.com.dynamiclight.genetic.view

import br.com.dynamiclight.genetic.app.Styles
import br.com.dynamiclight.genetic.domain.GAResult
import br.com.dynamiclight.genetic.domain.GaModel.MutationType
import br.com.dynamiclight.genetic.domain.Individual
import br.com.dynamiclight.genetic.viewmodel.GaViewModel
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.control.Alert
import javafx.scene.control.TextFormatter
import javafx.stage.FileChooser
import tornadofx.*
import tornadofx.FX.Companion.messages
import java.text.DecimalFormatSymbols

class UpdateGARequest(val result: GAResult<Triple<Int, Double, Individual>>) : FXEvent()

class MainLeftView : View(messages["panel.title"]) {
    private val viewModel: GaViewModel by inject()

    private val doubleFilter: (TextFormatter.Change) -> Boolean = { change ->
        val decimalSep = DecimalFormatSymbols.getInstance().decimalSeparator
            !change.isAdded || change.controlNewText.matches("\\d+(${decimalSep})?(\\d+)?".toRegex())
    }

    init {
        viewModel.evolutionResult = String.format(messages["evolutions.result.label"], 0)
        viewModel.shortestDistance = String.format(messages["shortest.distance.label"], 0.0f)

        subscribe<UpdateGARequest> {
            when (it.result) {
                is GAResult.Success -> {
                    val (evolutionCount, populationAverage, best) = it.result.data
                    viewModel.evolutionResult = String.format(messages["evolutions.result.label"], evolutionCount)
                    viewModel.shortestDistance = String.format(messages["shortest.distance.label"], best.fitness)

                    // Update Chart
                }
                is GAResult.Error -> alert(Alert.AlertType.ERROR, messages["error.title"], it.result.error.localizedMessage)
                else -> viewModel.status = messages["canceled"]
            }
        }
    }

    override val root = form {
        hbox {
            vbox {
                hbox(125) {
                    fieldset {
                        hboxConstraints { marginLeft = 20.0 }
                        vbox {
                            field(messages["population.size.label"]) {
                                textfield(viewModel.population) {
                                    alignment = Pos.CENTER_RIGHT
                                    maxWidth = 110.0
                                }
                            }
                            field(messages["crossover.rate.label"]) {
                                textfield(viewModel.crossoverRate) {
                                    alignment = Pos.CENTER_RIGHT
                                    maxWidth = 110.0
                                    filterInput(doubleFilter)
                                }
                            }
                            field(messages["mutation.rate.label"]) {
                                textfield(viewModel.mutationRate) {
                                    alignment = Pos.CENTER_RIGHT
                                    maxWidth = 110.0
                                    filterInput(doubleFilter)
                                }
                            }
                            field(messages["evolutions.label"]) {
                                textfield(viewModel.evolutions) {
                                    alignment = Pos.CENTER_RIGHT
                                    maxWidth = 110.0
                                }
                            }

                            fieldset(messages["mutation.fieldset.label"], labelPosition = Orientation.HORIZONTAL) {
                                vboxConstraints { marginTop = 22.0 }
                                vbox(5) {
                                    togglegroup {
                                        bind(viewModel.mutationType)
                                        radiobutton(messages["new.individual.radio.label"], value = MutationType.INDIVIDUAL) { isSelected = true }
                                        radiobutton(messages["general.population.radio.label"], value = MutationType.GENERAL)
                                        //radiobutton(messages["population.genes.radio.label"], value = MutationType.GENES)
                                    }
                                }
                            }

                            label(viewModel.evolutionResultProperty) {
                                vboxConstraints { marginTop = 38.0 }
                                addClass(Styles.subtitle)
                            }
                            label(viewModel.shortestDistanceProperty) { addClass(Styles.subtitle) }
                        }
                    }

                    fieldset {
                        vbox {
                            hbox {
                                field {
                                    hboxConstraints { marginTop = 4.0 }
                                    checkbox(messages["elitism.checkbox.label"], viewModel.useElitism)
                                }
                                field {
                                    textfield(viewModel.elitismCount) {
                                        alignment = Pos.CENTER_RIGHT
                                        hboxConstraints { marginLeft = 42.0 }
                                        maxWidth = 40.0
                                    }
                                }
                            }
                            field(messages["tournament.label"]) {
                                textfield(viewModel.tournament) {
                                    alignment = Pos.CENTER_RIGHT
                                    maxWidth = 40.0
                                }
                            }

                            button(messages["create.population.button"]) {
                                vboxConstraints { marginTop = 5.0 }
                                minWidth = 145.0
                                minHeight = 50.0
                                action {
                                    when (val result = viewModel.createPopulation()) {
                                        is GAResult.Error -> alert(Alert.AlertType.ERROR, messages["error.title"], result.error.localizedMessage)
                                        else -> viewModel.status = messages["population.created"]
                                    }
                                }
                            }

                            button(messages["run.stop.button"]) {
                                vboxConstraints { marginTop = 5.0 }
                                minWidth = 145.0
                                minHeight = 50.0
                                action {
                                    viewModel.commit()
                                    runAsync {
                                        viewModel.run()
                                    }
                                }
                            }

                            button(messages["clear.button"]) {
                                vboxConstraints { marginTop = 5.0 }
                                minWidth = 145.0
                                minHeight = 50.0
                                action {
                                    viewModel.item.cities.clear()
                                    fire(UpdateCitiesRequest)
                                }
                            }

                            button(messages["save.button"]) {
                                vboxConstraints { marginTop = 15.0 }
                                minWidth = 145.0
                                minHeight = 50.0
                                action {
                                    val fileChooser = FileChooser()
                                    val file = fileChooser.showSaveDialog(currentWindow)
                                    when (val result = viewModel.save(file)) {
                                        is GAResult.Success -> viewModel.status = messages["model.successfully.saved"]
                                        is GAResult.Error -> viewModel.status = result.error.localizedMessage
                                        else -> viewModel.status = messages["canceled"]
                                    }
                                }
                            }

                            button(messages["load.button"]) {
                                vboxConstraints { marginTop = 5.0 }
                                minWidth = 145.0
                                minHeight = 50.0
                                action {
                                    val fileChooser = FileChooser()
                                    val file = fileChooser.showOpenDialog(currentWindow)
                                    when (val result = viewModel.load(file)) {
                                        is GAResult.Success -> {
                                            fire(UpdateCitiesRequest)
                                            viewModel.status = messages["model.successfully.load"]
                                        }
                                        is GAResult.Error -> viewModel.status = result.error.localizedMessage
                                        else -> viewModel.status = messages["canceled"]
                                    }
                                }
                            }
                        }
                    }
                }

                linechart(null, CategoryAxis(), NumberAxis()) {
                    maxWidth = 500.0
                    minWidth = 500.0
                    minHeight = 300.0
                    maxHeight = 300.0
                    isLegendVisible = false
                    series("X axis") {
                        data("one", 150)
                        data("two", 250)
                        data("three", 80)
                    }
                }
            }

            separator(Orientation.VERTICAL) {
                hboxConstraints { marginLeft = 5.0 }
            }

        }
    }
}
