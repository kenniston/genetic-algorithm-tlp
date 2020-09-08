package br.com.dynamiclight.genetic.repository

import br.com.dynamiclight.genetic.domain.GaModel
import tornadofx.*
import java.io.File

class GaRepository: Component(), ScopedInstance {
    fun save(file: File, data: GaModel) {
        val json = data.toJSON().toString()
        println("Saving data...")
        println(json)
    }

    fun load(file: File) {
        TODO("Not yet implemented")
    }

}