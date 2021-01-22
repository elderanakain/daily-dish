package io.krugosvet.dailydish.common.core

import io.krugosvet.dailydish.BuildConfig
import io.krugosvet.dailydish.common.repository.MealRepository
import io.krugosvet.dailydish.common.repository.MealRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val environment: Environment = Environment.valueOf(if (BuildConfig.DEBUG) "DEV" else "PROD")

internal actual val platformModule: Module = module {

  single<MealRepository> {
    MealRepositoryImpl(get(), get(), get(), get())
  }
}
