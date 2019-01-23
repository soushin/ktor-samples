package com.github.soushin.ktorsample.routes

import com.github.soushin.ktorsample.util.logBuilder
import io.ktor.application.call
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

@KtorExperimentalLocationsAPI
@Location("/echo")
class Echo(val q: String? = null)

@KtorExperimentalLocationsAPI
fun Route.echo() {
    get<Echo> {
        call.apply {
            logBuilder.apply { element = mapOf("q" to request.queryParameters["q"]) }
            respond(request.queryParameters["q"] ?: "none")
        }
    }
}
