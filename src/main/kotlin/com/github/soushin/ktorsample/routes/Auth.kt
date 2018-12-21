package com.github.soushin.ktorsample.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.github.soushin.ktorsample.util.DateTimeUtil.getCurrentZonedDateTime
import com.github.soushin.ktorsample.util.user
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import org.threeten.bp.DateTimeUtils

@KtorExperimentalLocationsAPI
@Location("/auth")
class Auth() {
    @Location("/who")
    class Who

    @Location("/dummy-token")
    class DummyToken(val userId: String = "dummyId")
}

@KtorExperimentalLocationsAPI
fun Route.auth(jwtSecret: String) {
    authenticate {
        get<Auth.Who> { call.respond("Success, userId=${call.user?.id}") }
    }
    get<Auth.DummyToken> {
        call.apply {
            JWT.create()
                .withClaim("id", request.queryParameters["userId"])
                .withExpiresAt(DateTimeUtils.toDate(getCurrentZonedDateTime().plusDays(1L).toInstant()))
                .sign(Algorithm.HMAC256(jwtSecret)).let {
                    respond(it)
                }
        }
    }
}
