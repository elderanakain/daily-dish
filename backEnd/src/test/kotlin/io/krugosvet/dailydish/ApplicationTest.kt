package io.krugosvet.dailydish

import io.krugosvet.dailydish.common.core.currentDate
import io.krugosvet.dailydish.common.dto.Meal
import io.krugosvet.dailydish.common.repository.MealRepository
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlin.test.assertEquals
import org.junit.Test
import org.koin.ktor.ext.getKoin

class ApplicationTest {

    @Test
    fun `test response on server error`() = testApplication {
        // given

        application {
            module()
            getKoin().declare<MealRepository>(StubMealRepository())
        }

        // when

        val response = client.get("/meal")

        // then

        assertEquals(expected = HttpStatusCode.InternalServerError, actual = response.status)
    }

    @Test
    fun `test getting meals`() = testApplication {
        // given

        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json()
            }
        }

        val meals = listOf(
            Meal(id = "id", title = "title", description = "desc", lastCookingDate = currentDate),
        )

        application {
            module()
            getKoin().declare<MealRepository>(StubMealRepository().also { it.meals = meals })
        }

        // when

        val response = client.get("meal")

        // then

        assertEquals(expected = HttpStatusCode.OK, actual = response.status)
        assertEquals(expected = meals, actual = response.body())
    }

    @Test
    fun `test resetting`() = testApplication {
        // given

        val meals = listOf(
            Meal(id = "id", title = "title", description = "desc", lastCookingDate = currentDate),
        )

        application {
            module()
            getKoin().declare<MealRepository>(StubMealRepository().also { it.resetMeals = meals })
        }

        // when

        val response = client.get("reset")

        // then

        assertEquals(expected = HttpStatusCode.NoContent, actual = response.status)
    }

    @Test
    fun `test add a meal`() = testApplication {
        // given

        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json()
            }
        }

        val newMeal = Meal(
            id = "id",
            title = "title",
            description = "desc",
            lastCookingDate = currentDate,
        )

        application {
            module()
            getKoin().declare<MealRepository>(StubMealRepository())
        }

        // when

        val postResponse = client.post("meal") {
            contentType(ContentType.Application.Json)
            setBody(newMeal)
        }

        val getResponse = client.get("meal")

        // then

        assertEquals(expected = HttpStatusCode.Created, actual = postResponse.status)
        assertEquals(expected = HttpStatusCode.OK, actual = getResponse.status)
        assertEquals(expected = listOf(newMeal), actual = getResponse.body())
    }

    @Test
    fun `test update a meal`() = testApplication {
        // given

        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json()
            }
        }

        val initialMeal = Meal(
            id = "id",
            title = "title",
            description = "desc",
            lastCookingDate = currentDate,
        )

        val updatedMeal = Meal(
            id = "id",
            title = "newTitle",
            description = "desc",
            lastCookingDate = currentDate,
        )

        application {
            module()
            getKoin().declare<MealRepository>(
                StubMealRepository().also { it.meals = listOf(initialMeal) },
            )
        }

        // when

        val putResponse = client.put("meal") {
            contentType(ContentType.Application.Json)
            setBody(updatedMeal)
        }

        val getResponse = client.get("meal/id")

        // then

        assertEquals(expected = HttpStatusCode.Accepted, actual = putResponse.status)
        assertEquals(expected = HttpStatusCode.OK, actual = getResponse.status)
        assertEquals(expected = updatedMeal, actual = getResponse.body())
    }

    @Test
    fun `test deleting a meal`() = testApplication {
        // given

        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json()
            }
        }

        val meal = Meal(
            id = "id",
            title = "title",
            description = "desc",
            lastCookingDate = currentDate,
        )

        application {
            module()
            getKoin().declare<MealRepository>(StubMealRepository().also { it.meals = listOf(meal) })
        }

        // when

        val deleteResponse = client.delete("meal/id")
        val getResponse = client.get("meal")

        // then

        assertEquals(expected = HttpStatusCode.Accepted, actual = deleteResponse.status)
        assertEquals(expected = HttpStatusCode.OK, actual = getResponse.status)
        assertEquals(expected = emptyList<Meal>(), actual = getResponse.body())
    }
}
