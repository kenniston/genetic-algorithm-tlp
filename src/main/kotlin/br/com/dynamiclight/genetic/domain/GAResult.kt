package br.com.dynamiclight.genetic.domain

sealed class GAResult<out T: Any?> {
    data class Success<out T: Any?>(val data: T): GAResult<T>()
    data class Error(val error: Throwable): GAResult<Nothing>()
    data class Canceled(val message: String? = null): GAResult<Nothing>()
}