package io.krugosvet.dailydish.android.repository.injection

import androidx.room.Room
import io.krugosvet.dailydish.android.repository.db.DatabaseService
import io.krugosvet.dailydish.android.repository.db.meal.MealEntityFactory
import io.krugosvet.dailydish.android.repository.meal.MealFactory
import io.krugosvet.dailydish.android.repository.meal.MealRepository
import io.krugosvet.dailydish.android.repository.network.MealService
import io.krugosvet.dailydish.android.repository.network.createHttpClient
import org.koin.dsl.module

val repositoryModule = module {

  single {
    Room
      .databaseBuilder(get(), DatabaseService::class.java, "dd_database")
      .fallbackToDestructiveMigration()
      .build()
  }

  single {
    get<DatabaseService>().mealDao
  }

  single {
    MealRepository(get(), get(), get(), get())
  }

  single {
    createHttpClient()
  }

  single {
    MealService(get())
  }

  // Factories

  factory {
    MealEntityFactory()
  }

  factory {
    MealFactory()
  }
}
