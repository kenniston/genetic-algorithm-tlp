package br.com.dynamiclight.genetic.view

import tornadofx.*

class MainView : View("Genetic Algorithm") {
    override val root = borderpane {
        left<MainLeftView>()
        center<MainDrawView>()
        bottom<MainBottomView>()
    }
}
