package br.com.dynamiclight.genetic.view

import br.com.dynamiclight.genetic.app.Styles
import br.com.dynamiclight.genetic.domain.MutationType
import br.com.dynamiclight.genetic.viewmodel.GaViewModel
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.control.TextFormatter
import javafx.scene.paint.Color
import tornadofx.*
import java.text.DecimalFormatSymbols

class MainLeftView : View("Parameters") {
    private val viewModel: GaViewModel by inject()

    private val doubleFilter: (TextFormatter.Change) -> Boolean = { change ->
        val decimalSep = DecimalFormatSymbols.getInstance().decimalSeparator
        !change.isAdded || change.controlNewText.matches("\\d+(${decimalSep})?(\\d+)?".toRegex())
    }

    override val root = form {
        hbox {
            vbox {
                hbox (125) {
                    fieldset {
                        hboxConstraints { marginLeft = 20.0 }
                        vbox {
                            field("Population Size") {
                                textfield(viewModel.population) {
                                    alignment = Pos.CENTER_RIGHT
                                    maxWidth =  110.0
                                }
                            }
                            field("Crossover Rate") {
                                textfield(viewModel.crossoverRate) {
                                    alignment = Pos.CENTER_RIGHT
                                    maxWidth = 110.0
                                    filterInput(doubleFilter)
                                }
                            }
                            field("Mutation Rate") {
                                textfield(viewModel.mutationRate) {
                                    alignment = Pos.CENTER_RIGHT
                                    maxWidth = 110.0
                                    filterInput(doubleFilter)
                                }
                            }
                            field("Evolutions") {
                                textfield(viewModel.evolutions) {
                                    alignment = Pos.CENTER_RIGHT
                                    maxWidth = 110.0
                                }
                            }

                            fieldset("Mutation", labelPosition = Orientation.HORIZONTAL) {
                                vboxConstraints { marginTop = 22.0 }
                                vbox(5) {
                                    togglegroup {
                                        bind(viewModel.mutationType)
                                        radiobutton("New Individual", value = MutationType.INDIVIDUAL) { isSelected = true }
                                        radiobutton("General Population", value = MutationType.GENERAL)
                                        radiobutton("Population Genes", value = MutationType.GENES)
                                    }
                                }
                            }

                            label("Evolutions: 00") {
                                vboxConstraints { marginTop = 20.0 }
                                addClass(Styles.subtitle)
                            }
                            label("Shortest Distance: 00") { addClass(Styles.subtitle) }
                        }
                    }

                    fieldset {
                        vbox {
                            hbox {
                                field {
                                    hboxConstraints { marginTop = 4.0 }
                                    checkbox("Elitism", viewModel.useElitism)
                                }
                                field {
                                    textfield(viewModel.elitismCount) {
                                        alignment = Pos.CENTER_RIGHT
                                        hboxConstraints { marginLeft = 40.0 }
                                        maxWidth = 40.0
                                    }
                                }
                            }
                            field("Tournament") {
                                textfield(viewModel.tournament) {
                                    alignment = Pos.CENTER_RIGHT
                                    maxWidth = 40.0
                                }
                            }

                            button("Create Population") {
                                vboxConstraints { marginTop = 15.0 }
                                minWidth = 138.0
                                minHeight = 50.0
                                action {
                                    viewModel.commit()
                                }
                            }

                            button("Run / Stop") {
                                vboxConstraints { marginTop = 10.0 }
                                minWidth = 138.0
                                minHeight = 50.0
                                action {

                                }
                            }

                            button("Clear") {
                                vboxConstraints { marginTop = 10.0 }
                                minWidth = 138.0
                                minHeight = 50.0
                                action {

                                }
                            }

                        }
                    }
                }

                linechart(null, CategoryAxis(), NumberAxis()) {
                    maxWidth = 500.0
                    minWidth = 500.0
                    isLegendVisible = false
                    series("X axis") {
                        data("one", 150)
                        data("two", 250)
                        data("three", 80)
                    }
                }
            }

            separator (Orientation.VERTICAL)

        }
    }
}
