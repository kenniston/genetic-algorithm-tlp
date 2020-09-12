package br.com.dynamiclight.genetic.domain

import tornadofx.*
import javax.json.JsonNumber
import javax.json.JsonObject

data class Individual(var name: String, var chromosome: List<Int>, var fitness: Double, var position: Int) : JsonModel {
    override fun toJSON(json: JsonBuilder) {
        with(json) {
            add("name", name)
            add("chromosome", chromosome)
            add("fitness", fitness)
            add("position", position)
        }
    }

    override fun updateModel(json: JsonObject) {
        with(json) {
            name = string("name")!!
            chromosome = getJsonArray("chromosome").getValuesAs{ value: JsonNumber -> value.intValue()}
            fitness = double("fitness")!!
            position = int("position")!!
        }
    }
}