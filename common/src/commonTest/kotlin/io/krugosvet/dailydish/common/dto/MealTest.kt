package io.krugosvet.dailydish.common.dto

import kotlinx.datetime.toLocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class MealTest {

  @Test
  fun whenUpdateImageOnMeal_thenImageIsUpdated() {

    // given

    val meal = Meal("id", "title", "description", null, "2020-12-12".toLocalDate())

    val mockImage = "image"

    // when

    val updatedMeal = meal.updateImage(mockImage)

    // then

    assertEquals(mockImage, updatedMeal.image)
  }

  @Test
  fun whenUpdateImageOnAddMeal_thenImageIsUpdated() {

    // given

    val meal = AddMeal("title", "description", null, "2020-12-12".toLocalDate())

    val mockImage = "image"

    // when

    val updatedMeal = meal.updateImage(mockImage)

    // then

    assertEquals(mockImage, updatedMeal.image)
  }

}
