package io.krugosvet.dailydish.route

import io.krugosvet.dailydish.common.dto.Meal
import io.krugosvet.dailydish.common.repository.MealRepository
import io.krugosvet.dailydish.core.onCallFailure
import io.ktor.http.HttpStatusCode.Companion.Accepted
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.NoContent
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlinx.coroutines.flow.first
import org.koin.ktor.ext.inject

fun Route.mealRouting() {
    val mealRepository: MealRepository by inject()

    route("reset") {
        get {
            runCatching { mealRepository.reset() }
                .onSuccess { call.respond(NoContent) }
                .onCallFailure(call)
        }
    }

    route("meal") {
        get {
            runCatching { mealRepository.observe().first() }
                .onSuccess { meals -> call.respond(OK, meals) }
                .onCallFailure(call)
        }

        get("{id}") {
            val id = call.parameters["id"] ?: return@get

            runCatching { mealRepository.get(id) }
                .onSuccess { meal -> call.respond(OK, meal) }
                .onCallFailure(call)
        }

        post {
            val addMeal = call.receive<Meal>()

            runCatching { mealRepository.add(addMeal) }
                .onSuccess { id -> call.respond(Created, id) }
                .onCallFailure(call)
        }

        put {
            val meal = call.receive<Meal>()

            runCatching { mealRepository.update(meal) }
                .onSuccess { call.respond(Accepted) }
                .onCallFailure(call)
        }

        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete

            runCatching { mealRepository.delete(id) }
                .onSuccess { call.respond(Accepted) }
                .onCallFailure(call)
        }
    }
}
