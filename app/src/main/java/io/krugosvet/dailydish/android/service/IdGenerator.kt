package io.krugosvet.dailydish.android.service

import java.util.*

class IdGenerator {

  fun generate(): Long = UUID.randomUUID().hashCode().toLong()
}
