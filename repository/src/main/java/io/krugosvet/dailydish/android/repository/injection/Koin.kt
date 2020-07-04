package io.krugosvet.dailydish.android.repository.injection

import androidx.room.Room
import io.krugosvet.dailydish.android.repository.db.DatabaseService
import io.krugosvet.dailydish.android.repository.db.meal.MealEntityFactory
import io.krugosvet.dailydish.android.repository.meal.MealFactory
import io.krugosvet.dailydish.android.repository.meal.MealRepository
import io.krugosvet.dailydish.android.repository.sample.SampleDataPopulator
import org.koin.dsl.module

val repositoryModule = module {

  single {
    Room
      .databaseBuilder(get(), DatabaseService::class.java, "dd_database")
      .fallbackToDestructiveMigration()
      .addCallback(SampleDataPopulator(get(), get()))
      .build()
  }

  single {
    get<DatabaseService>().mealDao
  }

  single {
    MealRepository(get(), get(), get())
  }

  // Factories

  factory {
    MealEntityFactory()
  }

  factory {
    MealFactory(get(), get())
  }
}
