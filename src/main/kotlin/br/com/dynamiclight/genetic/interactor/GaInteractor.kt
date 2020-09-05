package br.com.dynamiclight.genetic.interactor

import br.com.dynamiclight.genetic.domain.GaModel
import br.com.dynamiclight.genetic.repository.GaRepository
import tornadofx.Component
import tornadofx.ScopedInstance

class GaInteractor: Component(), ScopedInstance {

    private val repository: GaRepository by inject()

    fun start(data: GaModel) {

    }

    fun save(data: GaModel) {
        repository.save(data)
    }

}