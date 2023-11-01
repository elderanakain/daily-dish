package io.krugosvet.dailydish.common

internal expect class IdGenerator() {

    fun generate(): String
}
