package io.krugosvet.dailydish.common.repository.db

import io.krugosvet.dailydish.common.IdGenerator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val dbModule = module {
    singleOf(::createDb)
    singleOf(::MealEntityFactory)
    singleOf(::IdGenerator)

    single {
        MealDao(get<Database>().dbQueries)
    }
}
