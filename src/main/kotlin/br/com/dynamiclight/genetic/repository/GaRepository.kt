package br.com.dynamiclight.genetic.repository

import br.com.dynamiclight.genetic.domain.GaModel
import tornadofx.Component
import tornadofx.ScopedInstance

class GaRepository: Component(), ScopedInstance {

    fun save(data: GaModel) {
        println("Saving data...")
    }

}