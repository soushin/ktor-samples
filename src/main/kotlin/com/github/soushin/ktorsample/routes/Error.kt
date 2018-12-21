package com.github.soushin.ktorsample.routes

import com.github.soushin.ktorsample.exception.BadRequestException
import com.github.soushin.ktorsample.exception.InternalServerErrorException
import com.github.soushin.ktorsample.exception.NotFoundException
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.routing.Route

@KtorExperimentalLocationsAPI
@Location("/errors")
class Errors() {
    @Location("/404")
    class NotFound

    @Location("/400")
    class BadRequest

    @Location("/500")
    class InternalServerError
}

@KtorExperimentalLocationsAPI
fun Route.error() {
    get<Errors.NotFound> { throw NotFoundException("not found") }
    get<Errors.BadRequest> { throw BadRequestException("bad request") }
    get<Errors.InternalServerError> { throw InternalServerErrorException("internal server error") }
}
