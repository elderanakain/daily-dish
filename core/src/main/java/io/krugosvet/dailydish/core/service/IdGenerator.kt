package io.krugosvet.dailydish.core.service

import java.util.*

class IdGenerator {

  fun generate(): Long = UUID.randomUUID().hashCode().toLong()
}
