package io.krugosvet.dailydish.common.core

import io.krugosvet.dailydish.common.repository.MealRepository
import io.krugosvet.dailydish.common.repository.MealRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    singleOf(::MealRepositoryImpl) bind MealRepository::class
}
