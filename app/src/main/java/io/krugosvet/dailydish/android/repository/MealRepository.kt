package io.krugosvet.dailydish.android.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.krugosvet.dailydish.android.db.meal.MealDao

interface IMealRepository {

  val meals: LiveData<List<Meal>>

  fun add(meal: Meal)

  fun update(meal: Meal)

  fun delete(meal: Meal)
}

class MealRepository(
  private val mealDao: MealDao,
  private val mealFactory: MealFactory
) :
  IMealRepository {

  override val meals: LiveData<List<Meal>> =
    Transformations.map(mealDao.getAll()) { mealEntities ->
      mealEntities.map { mealFactory.from(it) }
    }

  override fun add(meal: Meal) {
    mealDao.insert(mealFactory.to(meal))
  }

  override fun update(meal: Meal) {
    mealDao.update(mealFactory.to(meal))
  }

  override fun delete(meal: Meal) {
    mealDao.delete(mealFactory.to(meal))
  }
}
