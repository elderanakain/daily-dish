package io.krugosvet.dailydish

import io.krugosvet.dailydish.common.core.init
import io.krugosvet.dailydish.common.dto.AddMeal
import io.krugosvet.dailydish.common.dto.Meal
import io.krugosvet.dailydish.common.repository.MealRepository
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.append
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.ktor.utils.io.core.writeFully
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

private val mockMultipartHeader = "${ContentType.MultiPart.FormData}; boundary=boundary"

class MealTests : KoinTest {

    private val mealRepository: MealRepository by inject()

    @Before
    fun setup() {
        init()
    }

    @After
    fun tearDown() = runBlocking {
        mealRepository.reset()
    }

    @Test
    fun whenRequestMeals_thenValidCollectionIsReturned() = testApplication {
        // when

        val response = client.get("/meal")

        // then

        assertFalse(response.bodyAsText().isBlank())
        assertEquals(expected = HttpStatusCode.OK, actual = response.status)
    }

    @Test
    fun whenRequestMeal_thenValidMealIsReturned() = testApplication {
        // given

        val validMeal = mealRepository.meals.first()

        // when

        val response = client.get("/meal/${validMeal.id}")

        // then

        assertEquals(expected = validMeal, response.body())
        assertEquals(expected = HttpStatusCode.OK, response.status)
    }

    @Test
    fun whenRequestMeal_thenIdIsNotFound() = testApplication {
        // when

        val response = client.get("/meal/-1")

        // then

        assertNull(response.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun whenDeleteMeal_thenValidMealIsDeleted() = testApplication {
        // given

        val validMeal = mealRepository.meals.first()

        // when

        val response = client.delete("/meal/${validMeal.id}")

        // then

        assertEquals(HttpStatusCode.Accepted, response.status)
    }

    @Test
    fun whenAddMealWithNewImage_thenValidMealIsAdded() = testApplication {
        // given

        val mockTitle = "title"
        val mockDescription = "description"
        val mockLastCookingDate = LocalDate.parse("2020-01-01")

        val addMeal = AddMeal(mockTitle, mockDescription, image = null, mockLastCookingDate)

        val mockImage = File(this::class.java.getResource("mock_image.jpg")!!.toURI())

        val formData = formData {
            append("meal", Json.encodeToString(addMeal))
            append("meal_image", "", ContentType.Image.PNG, mockImage.length()) {
                writeFully(mockImage.readBytes())
            }
        }

        // when

        val createMealResponse = client.post("/meal") {
            headers.append(HttpHeaders.ContentType, mockMultipartHeader)
            setBody(formData)
        }

        // then

        assertEquals(HttpStatusCode.Created, createMealResponse.status)

        val createdMeal = mealRepository.get(createMealResponse.bodyAsText())

        assertEquals(expected = mockTitle, actual = createdMeal.title)
        assertEquals(expected = mockDescription, actual = createdMeal.description)
        assertTrue(
            createdMeal
                .image!!
                .matches("https://daily-dish-be-staging.herokuapp.com/static/.*".toRegex())
        )
        assertEquals(expected = mockLastCookingDate, actual = createdMeal.lastCookingDate)

        // when

        val mealImageId = "/".toPattern().split(createdMeal.image).last()

        val getImageResponse = client.get("/static/$mealImageId")

        // then

        assertEquals(expected = HttpStatusCode.OK, actual = getImageResponse.status)

        deleteMeal(createdMeal)
    }

    @Test
    fun whenUpdateWithImageMeal_thenChangesArePropagated() =
        testApplication {
            // given

            val mockTitle = "newTitle"

            val mockImage = File(this::class.java.getResource("mock_image.jpg")!!.toURI())

            val existingMeal = mealRepository.meals.first()
            val editedMeal = existingMeal.copy(title = mockTitle)

            val formData = formData {
                append("meal", Json.encodeToString(editedMeal))
                append("meal_image", "", ContentType.Image.PNG, mockImage.length()) {
                    writeFully(mockImage.readBytes())
                }
            }

            // when

            val response = client.put("/meal") {
                headers.append(HttpHeaders.ContentType, mockMultipartHeader)
                setBody(formData)
            }

            // then

            val savedMeal = mealRepository.get(editedMeal.id)

            assertEquals(HttpStatusCode.Accepted, response.status)
            assertEquals(editedMeal.copy(image = savedMeal.image), savedMeal)

            deleteMeal(savedMeal)
        }

    @Test
    fun whenUpdateMeal_thenChangesArePropagated() = testApplication {
        // given

        val mockTitle = "newTitle"

        val existingMeal = runBlocking { mealRepository.meals.first() }
        val editedMeal = existingMeal.copy(title = mockTitle)

        val formData = formData {
            append("meal", Json.encodeToString(editedMeal))
        }

        // when

        val response = client.put("/meal") {
            headers.append(HttpHeaders.ContentType, mockMultipartHeader)
            setBody(formData)
        }

        // then

        val savedMeal = mealRepository.get(editedMeal.id)

        assertEquals(HttpStatusCode.Accepted, response.status)
        assertEquals(editedMeal, savedMeal)
    }

    @Test
    fun whenUpdateWithDeletedImageMeal_thenChangesArePropagated() = testApplication {
        // given

        val mockImage = File(this::class.java.getResource("mock_image.jpg")!!.toURI())

        val existingMeal = runBlocking { mealRepository.meals.first() }

        var formData = formData {
            append("meal", Json.encodeToString(existingMeal))
            append("meal_image", "", ContentType.Image.PNG, mockImage.length()) {
                writeFully(mockImage.readBytes())
            }
        }

        // when

        var response = client.put("/meal") {
            headers.append(HttpHeaders.ContentType, mockMultipartHeader)
            setBody(formData)
        }

        // then

        var savedMeal = mealRepository.get(existingMeal.id)

        assertEquals(HttpStatusCode.Accepted, response.status)
        assertEquals(existingMeal.copy(image = savedMeal.image), savedMeal)

        // given

        val editedMeal = savedMeal.copy(image = null)

        formData = formData {
            append("meal", Json.encodeToString(editedMeal))
        }

        // when

        response = client.put("/meal") {
            headers.append(HttpHeaders.ContentType, mockMultipartHeader)
            setBody(formData)
        }

        // then

        savedMeal = mealRepository.get(editedMeal.id)

        assertEquals(HttpStatusCode.Accepted, response.status)
        assertEquals(editedMeal, savedMeal)

        assertFalse(getImageFile(existingMeal).exists())
    }

    private suspend fun ApplicationTestBuilder.deleteMeal(meal: Meal) {
        // when

        val deleteResponse = client.delete("/meal/${meal.id}")

        // then

        assertEquals(expected = HttpStatusCode.Accepted, actual = deleteResponse.status)
        assertFalse(actual = getImageFile(meal).exists())
    }

    private fun getImageFile(meal: Meal): File {
        val mealImageId = "/".toPattern().split(meal.image).last()

        return File("resources/static/$mealImageId")
    }
}
