package io.krugosvet.dailydish.common.repository.db

import org.koin.dsl.module

internal val dbModule = module {

  single {
    createDb()
  }

  single {
    MealDao(get<Database>().dbQueries)
  }

  factory {
    MealEntityFactory()
  }
}
