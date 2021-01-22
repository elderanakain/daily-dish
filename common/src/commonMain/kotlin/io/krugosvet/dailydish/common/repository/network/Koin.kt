package io.krugosvet.dailydish.common.repository.network

import io.krugosvet.dailydish.common.core.environment
import org.koin.dsl.module

internal val networkModule = module {

  single {
    createHttpClient()
  }

  single<MealService> {
    MealServiceImpl(environment.endpoint, get())
  }
}
