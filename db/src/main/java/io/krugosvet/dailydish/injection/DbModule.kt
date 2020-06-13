package io.krugosvet.dailydish.injection

import android.content.Context
import androidx.room.Room
import io.krugosvet.dailydish.DatabaseService
import io.krugosvet.dailydish.meal.MealDao
import org.koin.dsl.module

val dbModule = module {

  single<DatabaseService> {
    Room
      .databaseBuilder(get<Context>(), DatabaseService::class.java, "database")
      .build()
  }

  single<MealDao> {
    get<DatabaseService>().mealDao
  }

}
