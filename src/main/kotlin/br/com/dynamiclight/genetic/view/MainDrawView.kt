package br.com.dynamiclight.genetic.view

import br.com.dynamiclight.genetic.app.Styles
import br.com.dynamiclight.genetic.viewmodel.GaViewModel
import javafx.animation.Interpolator
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.util.Duration
import tornadofx.*
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
        val c = Color.rgb(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
        val circle = Circle(pt.x, pt.y, pointRadius, c)
        val ripple = Circle(pt.x, pt.y, pointRadius, c)
        circle.apply { animateFill(Duration.seconds(1.9), Color.TRANSPARENT, c) }
        ripple.apply { animateFill(Duration.seconds(1.0), c, Color.TRANSPARENT) }
        timeline {
            keyframe(Duration.seconds(0.35)) {
                keyvalue(circle.radiusProperty(), pointRadius, Interpolator.LINEAR)
            }
            keyframe(Duration.seconds(1.0)) {
                keyvalue(ripple.radiusProperty(), pointRadius.times(5), Interpolator.EASE_OUT)
                setOnFinished {
                    ripple.removeFromParent()
                }
            }
        }

        root.add(ripple)
        root.add(circle)
    }

}
