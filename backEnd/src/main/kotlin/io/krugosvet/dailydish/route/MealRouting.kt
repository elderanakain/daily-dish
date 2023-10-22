package io.krugosvet.dailydish.route

import io.krugosvet.dailydish.common.dto.Meal
import io.krugosvet.dailydish.common.repository.MealRepository
import io.krugosvet.dailydish.core.getIdFromParams
import io.krugosvet.dailydish.core.onCallFailure
import io.ktor.http.HttpStatusCode.Companion.Accepted
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.content.PartData
import io.ktor.http.content.readAllParts
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
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

fun Route.mealRouting() {
    val mealRepository: MealRepository by inject()

    route("reset") {
        get {
            mealRepository.reset()
            call.respond(OK)
        }
    }

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

        createMeal(mealRepository)
        updateMeal(mealRepository)

        delete("{id}") {
            val id = call.getIdFromParams() ?: return@delete

            runCatching { mealRepository.delete(id) }
                .onSuccess { call.respond(Accepted) }
                .onCallFailure(call)
        }
    }
}

private fun Route.createMeal(mealRepository: MealRepository) = post {
    val addMeal = call.receiveMealMultipart()

    runCatching { mealRepository.add(addMeal) }
        .onSuccess { id -> call.respond(Created, id) }
        .onCallFailure(call)
}

private fun Route.updateMeal(mealRepository: MealRepository) = put {
    val meal = call.receiveMealMultipart()

    runCatching { mealRepository.update(meal) }
        .onSuccess { call.respond(Accepted) }
        .onCallFailure(call)
}

private suspend inline fun ApplicationCall.receiveMealMultipart(): Meal {
    val multipart = receiveMultipart().readAllParts()

    return multipart.filterIsInstance<PartData.FormItem>().first().let { form ->
        Json.decodeFromString<Meal>(form.value)
            .also { form.dispose }
    }
}
