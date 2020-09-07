package br.com.dynamiclight.genetic.repository

import br.com.dynamiclight.genetic.domain.GaModel
import com.google.gson.Gson
import tornadofx.*
import java.io.File

class GaRepository: Component(), ScopedInstance {
    private val gson = Gson()

    fun save(file: File, data: GaModel) {
        val json = gson.toJson(data)
        println("Saving data...")
        println(json)
    }

    fun load(file: File) {
        TODO("Not yet implemented")
    }

}