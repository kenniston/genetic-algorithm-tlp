package br.com.dynamiclight.genetic.interactor

import br.com.dynamiclight.genetic.domain.GaModel
import br.com.dynamiclight.genetic.domain.Point
import br.com.dynamiclight.genetic.repository.GaRepository
import tornadofx.*
import java.io.File
import kotlin.math.pow
import kotlin.math.sqrt

class GaInteractor : Component(), ScopedInstance {
    private val repository: GaRepository by inject()

    private var data = GaModel()
    val model: GaModel
        get() = data

    fun save(file: File) {
        repository.save(file, data)
    }

    fun addPoint(x: Double, y: Double, radius: Double, color: String) {
        data.points.add(Point(x, y, radius, color))
        generateDistanceData()
    }

    private fun generateDistanceData() {
        data.pointsDistance.clear()
        data.points.forEach { first ->
            data.points.filter { it != first }.forEach { second ->
                data.pointsDistance["${first.hashCode()}.${second.hashCode()}"] =
                        sqrt((second.x - first.x).pow(2.0) + (second.y - first.y).pow(2.0))
            }
        }
    }

    fun load(file: File) {
        repository.load(file)
    }

}