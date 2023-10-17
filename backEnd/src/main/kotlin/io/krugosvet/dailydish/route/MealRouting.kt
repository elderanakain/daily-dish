package io.krugosvet.dailydish.route

import io.krugosvet.dailydish.common.dto.AddMeal
import io.krugosvet.dailydish.common.dto.IMeal
import io.krugosvet.dailydish.common.dto.Meal
import io.krugosvet.dailydish.common.dto.NewImage
import io.krugosvet.dailydish.common.repository.MealRepository
import io.krugosvet.dailydish.core.getIdFromParams
import io.krugosvet.dailydish.core.onCallFailure
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode.Companion.Accepted
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.content.PartData
import io.ktor.http.content.readAllParts
import io.ktor.http.content.streamProvider
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

fun Route.mealRouting() {
    val mealRepository: MealRepository by inject()

    route("meal") {
        get {
            runCatching { mealRepository.meals }
                .onSuccess { meals -> call.respond(OK, meals) }
                .onCallFailure(call)
        }

        get("{id}") {
            val id = call.getIdFromParams() ?: return@get

            runCatching { mealRepository.get(id) }
                .onSuccess { meal -> call.respond(OK, meal) }
                .onCallFailure(call)
        }

        createMealRoute(mealRepository)
        updateMealRoute(mealRepository)

        delete("{id}") {
            val id = call.getIdFromParams() ?: return@delete

            runCatching { mealRepository.delete(id) }
                .onSuccess { call.respond(Accepted) }
                .onCallFailure(call)
        }
    }
}

private fun Route.createMealRoute(mealRepository: MealRepository) = post {
    withContext(Dispatchers.IO) {
        val (addMeal, newImage) = call.receiveMealMultipart<AddMeal>()

        runCatching { mealRepository.add(addMeal, newImage) }
            .onSuccess { id -> call.respond(Created, id) }
            .onCallFailure(call)
    }
}

private fun Route.updateMealRoute(mealRepository: MealRepository) = put {
    withContext(Dispatchers.IO) {
        val (meal, newImage) = call.receiveMealMultipart<Meal>()

        runCatching { mealRepository.update(meal, newImage) }
            .onSuccess { call.respond(Accepted) }
            .onCallFailure(call)
    }
}

private suspend inline fun <reified T : IMeal> ApplicationCall.receiveMealMultipart(): Pair<T, NewImage?> {
    val multipart = receiveMultipart().readAllParts()

    val meal = multipart.filterIsInstance<PartData.FormItem>().first().let { form ->
        Json.decodeFromString<T>(form.value)
            .also { form.dispose }
    }

    val image = multipart.filterIsInstance<PartData.FileItem>().firstOrNull()?.let { file ->
        val imageExtension = file.contentType?.contentSubtype ?: ContentType.Image.JPEG.toString()

        val bytes = file.streamProvider().use {
            it.readBytes()
        }

        NewImage(bytes, imageExtension)
            .also { file.dispose }
    }

    return meal to image
}
