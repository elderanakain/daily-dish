package io.krugosvet.dailydish.android.repository.network

import io.krugosvet.dailydish.android.repository.BuildConfig
import io.krugosvet.dailydish.android.repository.meal.AddMeal
import io.krugosvet.dailydish.android.repository.meal.IMeal
import io.krugosvet.dailydish.android.repository.meal.Meal
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val MEAL_ENDPOINT = "${BuildConfig.API_BASE_URL}/meal"
private const val MULTIPART_KEY_MEAL = "meal"
private const val MULTIPART_KEY_MEAL_IMAGE = "meal_image"

class MealService(
  private val httpClient: HttpClient,
  private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

  suspend fun getAll(): List<Meal> = withContext(ioDispatcher) {
    httpClient.get(MEAL_ENDPOINT)
  }

  suspend fun delete(meal: Meal): Unit = withContext(ioDispatcher) {
    httpClient.delete("$MEAL_ENDPOINT/${meal.id}")
  }

  suspend fun add(meal: AddMeal, newImage: ByteArray?): Unit = withContext(ioDispatcher) {
    httpClient.submitFormWithBinaryData(MEAL_ENDPOINT, createMealForm(meal, newImage))
  }

  suspend fun update(meal: Meal, newImage: ByteArray?): Unit = withContext(ioDispatcher) {
    httpClient.put(MEAL_ENDPOINT) {
      body = MultiPartFormDataContent(createMealForm(meal, newImage))
    }
  }

  private inline fun <reified T : IMeal> createMealForm(meal: T, image: ByteArray?) =
    formData {
      appendMeal(meal)
      appendImage(image)
    }

  private inline fun <reified T : IMeal> FormBuilder.appendMeal(meal: T) {
    append(MULTIPART_KEY_MEAL, Json.encodeToString(meal))
  }

  private fun FormBuilder.appendImage(newImage: ByteArray?) {
    newImage ?: return

    append(MULTIPART_KEY_MEAL_IMAGE, "", ContentType.Image.JPEG, newImage.size.toLong()) {
      writeFully(newImage)
    }
  }
}
