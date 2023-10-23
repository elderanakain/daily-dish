package io.krugosvet.dailydish.common

import java.util.UUID

public actual class IdGenerator {

    public actual fun generate(): String = UUID.randomUUID().toString()
}
