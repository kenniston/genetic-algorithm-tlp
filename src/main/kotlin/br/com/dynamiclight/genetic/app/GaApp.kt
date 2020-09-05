package br.com.dynamiclight.genetic.app

import br.com.dynamiclight.genetic.view.MainView
import javafx.stage.Stage
import tornadofx.App

class GaApp: App(MainView::class, Styles::class) {
    override fun start(stage: Stage) {
        with(stage) {
            minWidth = 1480.0
            minHeight = 720.0
            isResizable = false
            super.start(this)
        }
    }
}