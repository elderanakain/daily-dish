package io.krugosvet.dailydish.common.repository

import io.krugosvet.dailydish.common.dto.AddMeal
import io.krugosvet.dailydish.common.dto.Meal
import io.krugosvet.dailydish.common.dto.MealFactory
import io.krugosvet.dailydish.common.dto.NewImage
import io.krugosvet.dailydish.common.repository.db.MealDao
import io.krugosvet.dailydish.common.repository.db.MealEntityFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

internal actual class MealRepositoryImpl(
  private val mealDao: MealDao,
  private val mealFactory: MealFactory,
  private val imageRepository: ImageRepository,
  private val mealEntityFactory: MealEntityFactory,
) :
  MealRepository {

  override val meals: List<Meal>
    get() = mealDao.meals
      .map(mealFactory::from)

  override val mealsFlow: Flow<List<Meal>>
    get() = mealDao.mealsFlow
      .map {
        it.map(mealFactory::from)
      }

  override suspend fun add(meal: AddMeal, newImage: NewImage?): String {
    val newId = UUID.randomUUID().toString()
    var imageUri = meal.image

    if (newImage != null) {
      imageUri = imageRepository.save(newImage)
    }

    val newEntity = mealEntityFactory.from(
      meal.copy(image = imageUri),
      newId
    )

    mealDao.add(newEntity)

    return newId
  }

  override suspend fun delete(mealId: String) {
    val meal = mealDao.get(mealId)

    imageRepository.delete(meal.imageUri)

    mealDao.delete(mealId)
  }

  override suspend fun get(mealId: String): Meal {
    val entity = mealDao.get(mealId)

    return mealFactory.from(entity)
  }

  override suspend fun update(meal: Meal, newImage: NewImage?) {
    val mealEntity = mealDao.get(meal.id)

    val imageUri = when {
      meal.image == null -> {
        imageRepository.delete(mealEntity.imageUri)
        null
      }
      newImage != null -> imageRepository.save(newImage)
      else -> meal.image
    }

    val updatedEntity = mealEntity.copy(
      title = meal.title,
      description = meal.description,
      imageUri = imageUri,
      lastCookingDate = meal.lastCookingDate.toString(),
    )

    mealDao.update(updatedEntity)
  }

  override suspend fun fetch() {
    // TODO: Fix it
  }

  override suspend fun reset() {
    mealDao.reset()
  }
}
