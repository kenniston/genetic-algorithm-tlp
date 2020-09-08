package br.com.dynamiclight.genetic.view

import br.com.dynamiclight.genetic.viewmodel.GaViewModel
import javafx.animation.Interpolator
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.util.Duration
import tornadofx.*
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class MainDrawView : View("Drawer") {
    private val viewModel: GaViewModel by inject()
    private val pointRadius = 10.0

    override val root = pane {
        addEventHandler(MouseEvent.MOUSE_PRESSED, ::addPoint)
        label("Left mouse click to create a point")
    }

    private fun addPoint(evt: MouseEvent) {
        val pt = root.sceneToLocal(evt.sceneX, evt.sceneY)

        if (isNearToPoint(pt.x, pt.y)) return

        val c = Color.rgb(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
        val city = Circle(pt.x, pt.y, pointRadius, c)
        val ripple = Circle(pt.x, pt.y, pointRadius, c)
        city.apply { animateFill(Duration.seconds(1.9), Color.TRANSPARENT, c) }
        ripple.apply { animateFill(Duration.seconds(1.0), c, Color.TRANSPARENT) }
        timeline {
            keyframe(Duration.seconds(0.35)) {
                keyvalue(city.radiusProperty(), pointRadius, Interpolator.LINEAR)
            }
            keyframe(Duration.seconds(1.0)) {
                keyvalue(ripple.radiusProperty(), pointRadius.times(5), Interpolator.EASE_OUT)
                setOnFinished {
                    ripple.removeFromParent()
                }
            }
        }

        root.add(ripple)
        root.add(city)
        viewModel.addCity(city)
    }

    private fun isNearToPoint(x: Double, y: Double): Boolean {
        viewModel.item.cities.forEach {
            val distance = sqrt((it.x - x).pow(2.0) + (it.y - y).pow(2.0))
            if (distance <= pointRadius * 2) {
                return true
            }
        }
        return false
    }

}
