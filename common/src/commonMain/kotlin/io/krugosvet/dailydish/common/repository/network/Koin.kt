package io.krugosvet.dailydish.common.repository.network

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val networkModule = module {

    singleOf(::createHttpClient)

    single<MealService> {
        MealServiceImpl(baseUrl, get())
    }
}
