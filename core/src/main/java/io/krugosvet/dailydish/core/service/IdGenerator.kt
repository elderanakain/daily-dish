package io.krugosvet.dailydish.core.service

import java.util.*

class IdGenerator {

  fun generate(): String = UUID.randomUUID().toString()
}
