package io.krugosvet.dailydish.common.repository.db

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val dbModule = module {
    singleOf(::createDb)
    singleOf(::MealEntityFactory)

    single {
        MealDao(get<Database>().dbQueries)
    }
}
