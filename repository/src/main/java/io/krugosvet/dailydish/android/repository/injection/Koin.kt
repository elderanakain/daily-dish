package io.krugosvet.dailydish.android.repository.injection

import androidx.room.Room
import io.krugosvet.dailydish.android.repository.db.DatabaseService
import io.krugosvet.dailydish.android.repository.meal.MealFactory
import io.krugosvet.dailydish.android.repository.meal.MealRepository
import org.koin.dsl.module

val repositoryModule = module {

  single {
    Room
      .databaseBuilder(get(), DatabaseService::class.java, "database")
      .build()
  }

  single {
    get<DatabaseService>().mealDao
  }

  single {
    MealFactory(get(), get())
  }

  single {
    MealRepository(get(), get())
  }

}
