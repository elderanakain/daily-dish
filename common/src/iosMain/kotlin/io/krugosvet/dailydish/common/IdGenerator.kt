package io.krugosvet.dailydish.common

import platform.Foundation.NSUUID

internal actual class IdGenerator {

    actual fun generate(): String =
        NSUUID().UUIDString
}
