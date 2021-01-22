package io.krugosvet.dailydish.common.repository.network

import io.krugosvet.dailydish.common.core.parse
import io.krugosvet.dailydish.common.dto.AddMeal
import io.krugosvet.dailydish.common.dto.IMeal
import io.krugosvet.dailydish.common.dto.Meal
import io.krugosvet.dailydish.common.dto.NewImage
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormBuilder
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.append
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.http.ContentType
import io.ktor.utils.io.core.writeFully
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal interface MealService {

  suspend fun getAll(): List<Meal>

  suspend fun get(mealId: String): Meal

  suspend fun delete(mealId: String)

  suspend fun add(meal: AddMeal, newImage: NewImage?): String

  suspend fun update(meal: Meal, newImage: NewImage?)
}

internal class MealServiceImpl(
  baseEndpoint: String,
  private val httpClient: HttpClient,
) :
  MealService {

  private val endpoint = "$baseEndpoint/meal"

  override suspend fun getAll(): List<Meal> =
    httpClient.get(endpoint)

  override suspend fun get(mealId: String): Meal =
    httpClient.get("$endpoint/$mealId")

  override suspend fun delete(mealId: String): Unit =
    httpClient.delete("$endpoint/$mealId")

  override suspend fun add(meal: AddMeal, newImage: NewImage?): String =
    httpClient.submitFormWithBinaryData(endpoint, createMealForm(meal, newImage))

  override suspend fun update(meal: Meal, newImage: NewImage?) {
    httpClient.put<Unit>(endpoint) {
      body = MultiPartFormDataContent(createMealForm(meal, newImage))
    }
  }

  private inline fun <reified T : IMeal> createMealForm(meal: T, image: NewImage?) =
    formData {
      appendMeal(meal)
      appendImage(image)
    }

  private inline fun <reified T : IMeal> FormBuilder.appendMeal(meal: T) {
    append(MULTIPART_KEY_MEAL, Json.encodeToString(meal))
  }

  private fun FormBuilder.appendImage(newImage: NewImage?) {
    newImage ?: return

    val newImageData = newImage.data
    val contentType = ContentType.Image.parse(newImage.extension)

    append(MULTIPART_KEY_MEAL_IMAGE, "", contentType, newImageData.size.toLong()) {
      writeFully(newImageData)
    }
  }
}

private const val MULTIPART_KEY_MEAL = "meal"
private const val MULTIPART_KEY_MEAL_IMAGE = "meal_image"
