package io.krugosvet.dailydish.common

import java.util.UUID

internal actual class IdGenerator {

    actual fun generate(): String = UUID.randomUUID().toString()
}
