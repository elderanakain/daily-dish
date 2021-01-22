package io.krugosvet.dailydish.common.core

import io.krugosvet.dailydish.common.repository.ImageRepository
import io.krugosvet.dailydish.common.repository.MealRepository
import io.krugosvet.dailydish.common.repository.MealRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val environment: Environment = Environment.valueOf(System.getenv("KTOR_ENV"))

internal actual val platformModule: Module = module {

  single {
    ImageRepository(environment.endpoint)
  }

  single<MealRepository> {
    MealRepositoryImpl(get(), get(), get(), get())
  }
}
