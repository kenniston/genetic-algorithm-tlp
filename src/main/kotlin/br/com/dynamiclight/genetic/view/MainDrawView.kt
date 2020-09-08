package br.com.dynamiclight.genetic.view

import br.com.dynamiclight.genetic.viewmodel.GaViewModel
import io.github.serpro69.kfaker.Faker
import javafx.animation.Interpolator
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.util.Duration
import tornadofx.*
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

object UpdateCitiesRequest : FXEvent()

class MainDrawView : View("Drawer") {
    private val viewModel: GaViewModel by inject()
    private val pointRadius = 10.0
    private val faker = Faker()

    override val root = pane {
        vbox {
            label(messages["drawer.message"])
            label(viewModel.citiesProperty)
        }

        addEventHandler(MouseEvent.MOUSE_PRESSED, ::addPoint)
        subscribe<UpdateCitiesRequest> {
            loadCities()
        }
    }

    private fun addPoint(evt: MouseEvent) {
        val pt = root.sceneToLocal(evt.sceneX, evt.sceneY)

        if (isNearToPoint(pt.x, pt.y)) return

        val city = addCity(pt.x, pt.y, pointRadius)
        viewModel.addCity(city)
    }

    private fun addCity(x: Double, y: Double, radius: Double, color: String? = null, name: String? = null) : Circle {
        val c = if (color != null) {
            c(color)
        } else Color.rgb(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
        val city = Circle(x, y, radius, c)
        val ripple = Circle(x, y, radius, c)
        city.apply { animateFill(Duration.seconds(1.9), Color.TRANSPARENT, c) }
        ripple.apply { animateFill(Duration.seconds(1.0), c, Color.TRANSPARENT) }
        timeline {
            keyframe(Duration.seconds(0.35)) {
                keyvalue(city.radiusProperty(), radius, Interpolator.LINEAR)
            }
            keyframe(Duration.seconds(1.0)) {
                keyvalue(ripple.radiusProperty(), radius.times(5), Interpolator.EASE_OUT)
                setOnFinished {
                    ripple.removeFromParent()
                }
            }
        }

        root.add(ripple)
        root.add(city)

        city.id = name ?: faker.address.city()
        city.add(label(city.id) {
            minWidth = 200.0
            maxWidth = 200.0
            alignment = Pos.CENTER
            relocate(city.centerX - maxWidth / 2, city.centerY + city.radius)
        })

        return city
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

    private fun loadCities() {
        root.children.filter { it is Circle || it is Label }.forEach { it.removeFromParent() }
        viewModel.item.cities.forEach { city ->
            addCity(city.x, city.y, city.radius, city.color, city.name)
        }
    }

}
