package io.krugosvet.dailydish.common.repository.network

import io.krugosvet.dailydish.common.dto.Meal
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormBuilder
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal interface MealService {

    suspend fun getAll(): List<Meal>

    suspend fun get(mealId: String): Meal

    suspend fun delete(mealId: String)

    suspend fun add(meal: Meal): String

    suspend fun update(meal: Meal)
}

internal class MealServiceImpl(
    baseEndpoint: String,
    private val httpClient: HttpClient,
) :
    MealService {

    private val endpoint = "$baseEndpoint/meal"

    override suspend fun getAll(): List<Meal> =
        httpClient.get(endpoint).body()

    override suspend fun get(mealId: String): Meal =
        httpClient.get("$endpoint/$mealId").body()

    override suspend fun delete(mealId: String): Unit =
        httpClient.delete("$endpoint/$mealId").body()

    override suspend fun add(meal: Meal): String =
        httpClient.submitFormWithBinaryData(endpoint, createMealForm(meal)).body()

    override suspend fun update(meal: Meal) {
        httpClient.put(endpoint) {
            setBody(createMealForm(meal))
        }
    }

    private fun createMealForm(meal: Meal) = formData {
        appendMeal(meal)
    }

    private fun FormBuilder.appendMeal(meal: Meal) {
        append(MULTIPART_KEY_MEAL, Json.encodeToString(meal))
    }
}

private const val MULTIPART_KEY_MEAL = "meal"
private const val MULTIPART_KEY_MEAL_IMAGE = "meal_image"
