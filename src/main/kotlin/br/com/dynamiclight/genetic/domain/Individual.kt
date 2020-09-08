package br.com.dynamiclight.genetic.domain

import tornadofx.*
import javax.json.JsonNumber
import javax.json.JsonObject

data class Individual(var chromosome: List<Int>, var fitness: Double) : JsonModel {
    override fun toJSON(json: JsonBuilder) {
        with(json) {
            add("chromosome", chromosome)
            add("fitness", fitness)
        }
    }

    override fun updateModel(json: JsonObject) {
        with(json) {
            chromosome = getJsonArray("chromosome").getValuesAs{ value: JsonNumber -> value.intValue()}
            fitness = double("fitness")!!
        }
    }
}