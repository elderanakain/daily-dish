package io.krugosvet.dailydish.android.repository.injection

import android.content.Context
import androidx.room.Room
import io.krugosvet.dailydish.android.repository.db.DatabaseService
import io.krugosvet.dailydish.android.repository.db.meal.MealDao
import io.krugosvet.dailydish.android.repository.meal.MealFactory
import io.krugosvet.dailydish.android.repository.meal.MealRepository
import io.krugosvet.dailydish.core.service.DateService
import io.krugosvet.dailydish.core.service.IdGenerator
import org.koin.dsl.module

@Suppress("RemoveExplicitTypeArguments")
val repositoryModule = module {

  single<DatabaseService> {
    Room
      .databaseBuilder(get<Context>(), DatabaseService::class.java, "database")
      .build()
  }

  single<MealDao> {
    get<DatabaseService>().mealDao
  }

  single<MealFactory> {
    MealFactory(
      get<DateService>(),
      get<IdGenerator>()
    )
  }

  single<MealRepository> {
    MealRepository(
      get<MealDao>(),
      get<MealFactory>()
    )
  }

}
