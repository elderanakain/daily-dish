package io.krugosvet.dailydish.common

import java.util.*

public actual class IdGenerator {

    public actual fun generate(): String = UUID.randomUUID().toString()
}
