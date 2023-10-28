package io.krugosvet.dailydish

import io.krugosvet.dailydish.common.Environment
import org.koin.dsl.module

val module = module {
    val environment = Environment.init()

    single { environment.mealRepository }
}
