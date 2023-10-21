package io.krugosvet.dailydish.common.usecase

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val useCaseModule = module {
    factoryOf(::AddMealUseCase)
    factoryOf(::DeleteMealUseCase)
    factoryOf(::SetCurrentTimeToCookedDateMealUseCase)
}
