package br.com.dynamiclight.genetic.domain

import tornadofx.*
import javax.json.JsonObject

data class City(
    var name: String,
    var x: Double,
    var y: Double,
    var radius: Double,
    var color: String
) : JsonModel {

    constructor() : this("", 0.0, 0.0, 0.0, "")

    override fun toJSON(json: JsonBuilder) {
        with(json) {
            add("name", name)
            add("x", x)
            add("y", y)
            add("radius", radius)
            add("color", color)
        }
    }

    override fun updateModel(json: JsonObject) {
        with(json) {
            name = string("name")!!
            x = double("x")!!
            y = double("y")!!
            radius = double("radius")!!
            color = string("color")!!
        }
    }
}