package io.krugosvet.dailydish.core

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.log
import io.ktor.server.response.respond
import io.ktor.util.logging.error

suspend inline fun <T> Result<T>.onCallFailure(call: ApplicationCall): Result<T> =
    onFailure {
        call.application.log.error(it)
        call.respond(HttpStatusCode.InternalServerError)
    }
