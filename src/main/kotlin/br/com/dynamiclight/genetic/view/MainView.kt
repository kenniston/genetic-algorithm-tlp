package br.com.dynamiclight.genetic.view

import br.com.dynamiclight.genetic.app.Styles
import tornadofx.*

class MainView : View("Genetic Algorithm") {
    override val root = borderpane {
        left<MainLeftView>()
        center<MainDrawView>()
    }
}

class BottomView: View() {
    override val root = label("Bottom View") {
        addClass(Styles.heading)
    }
}