package io.krugosvet.dailydish.common.core

import io.krugosvet.dailydish.common.repository.MealRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {

    single {
        MealRepositoryImpl(get(), get(), get(), get())
    }
}