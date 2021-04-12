package io.krugosvet.dailydish.common

import platform.Foundation.NSUUID

public actual class IdGenerator {

    public actual fun generate(): String =
        NSUUID().UUIDString

}
