package io.krugosvet.dailydish

import io.krugosvet.dailydish.route.mealRouting
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.gzip
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.routing.routing
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(CIO, port = 9080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

internal fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)

    install(Koin) {
        modules(module)
    }

    install(ContentNegotiation) {
        json()
    }

    install(Compression) {
        gzip { priority = 1.0 }
    }

    routing {
        mealRouting()
    }
}
