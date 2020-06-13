package io.krugosvet.dailydish

import androidx.room.Database
import androidx.room.RoomDatabase
import io.krugosvet.dailydish.meal.MealDao
import io.krugosvet.dailydish.meal.MealEntity

@Database(entities = [MealEntity::class], version = 1, exportSchema = false)
abstract class DatabaseService: RoomDatabase() {

  abstract val mealDao: MealDao
}
