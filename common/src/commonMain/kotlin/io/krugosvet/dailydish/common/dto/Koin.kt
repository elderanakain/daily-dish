package io.krugosvet.dailydish.common.dto

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val dtoModule = module {
    factoryOf(::AddMealFormValidator)
    factoryOf(::MealFactory)
}
