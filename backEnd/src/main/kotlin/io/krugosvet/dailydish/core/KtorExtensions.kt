package io.krugosvet.dailydish.core

import io.ktor.http.*
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.log
import io.ktor.server.response.respond
import io.ktor.util.logging.error

suspend inline fun <T> Result<T>.onCallFailure(call: ApplicationCall): Result<T> =
    onFailure {
        call.application.log.error(it)
        call.respond(HttpStatusCode.BadRequest)
    }

suspend fun ApplicationCall.getIdFromParams(): String? {
    val id = parameters["id"]?.takeIf { it.isNotBlank() }

    if (id == null) {
        respond(HttpStatusCode.BadRequest)
        return null
    }

    return id
}
