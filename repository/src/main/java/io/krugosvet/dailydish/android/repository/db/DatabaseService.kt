package io.krugosvet.dailydish.android.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.krugosvet.dailydish.android.repository.db.meal.MealDao
import io.krugosvet.dailydish.android.repository.db.meal.MealEntity

@Database(entities = [MealEntity::class], version = 1, exportSchema = false)
abstract class DatabaseService: RoomDatabase() {

  abstract val mealDao: MealDao
}
