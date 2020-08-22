package br.com.dynamiclight.genetic.view

import br.com.dynamiclight.genetic.app.Styles
import tornadofx.*

class MainView : View("Genetic Algorithm") {
    override val root = hbox {
        label(title) {
            addClass(Styles.heading)
        }
    }
}