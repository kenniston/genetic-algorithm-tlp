package br.com.dynamiclight.genetic.app

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val subtitle by cssclass()
        val paintArea by cssclass()
        val status by cssclass()
    }

    init {
        label and heading {
            padding = box(10.px)
            fontSize = 62.px
            fontWeight = FontWeight.BOLD
        }

        subtitle {
            fontSize = 20.px
        }

        paintArea {
            backgroundColor += Color.LIGHTGRAY
        }

        status {
            fontWeight = FontWeight.BOLD
        }
    }
}