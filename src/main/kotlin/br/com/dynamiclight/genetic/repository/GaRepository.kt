package br.com.dynamiclight.genetic.repository

import br.com.dynamiclight.genetic.domain.GAResult
import br.com.dynamiclight.genetic.domain.GaModel
import tornadofx.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception

class GaRepository: Component(), ScopedInstance {
    fun save(file: File, data: GaModel) : GAResult<Unit> {
        return try {
            val json = data.toJSON()
            val out = FileOutputStream(file)
            json.save(out)
            return GAResult.Success(Unit)
        } catch (e: Exception) {
            GAResult.Error(e)
        }
    }

    fun load(file: File) : GAResult<GaModel> {
        return try {
            val input = FileInputStream(file)
            val model = loadJsonObject(input).toModel<GaModel>()
            return GAResult.Success(model)
        } catch (e: Exception) {
            GAResult.Error(e)
        }
    }

}