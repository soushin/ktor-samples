package com.github.soushin.ktorsample.routes

import com.github.soushin.ktorsample.usecase.github.RepositoryUseCase
import io.ktor.application.call
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

@KtorExperimentalLocationsAPI
@Location("/repository")
class Repository()

@KtorExperimentalLocationsAPI
fun Route.repository(usecase: RepositoryUseCase) {
    get<Repository> {
        call.respond(usecase.getRepositoryWithPullRequests("owner", 1))
    }
}
