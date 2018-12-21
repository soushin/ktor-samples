package com.github.soushin.ktorsample.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.github.soushin.ktorsample.authModule
import com.github.soushin.ktorsample.util.DateTimeUtil
import io.ktor.config.MapApplicationConfig
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.util.KtorExperimentalAPI
import org.junit.Before
import org.junit.Test
import org.threeten.bp.Clock
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.ZonedDateTime
import java.util.*
import kotlin.test.assertEquals

@KtorExperimentalLocationsAPI
@KtorExperimentalAPI
class AuthTest {

    companion object {
        private const val JWT_SECRET = "secret"
    }

    private lateinit var engine: TestApplicationEngine
    private lateinit var userId: String
    private lateinit var authToken: String

    @Before
    fun before() {
        val clock = Clock.fixed(
            ZonedDateTime.of(2018, 12, 22, 0, 0, 0, 0, DateTimeUtil.ZONE_JST).toInstant(),
            DateTimeUtil.ZONE_JST
        )
        DateTimeUtil.init(clock, Locale.JAPAN)

        userId = "1"
        authToken = JWT.create()
            .withClaim("id", userId)
            .withExpiresAt(DateTimeUtils.toDate(DateTimeUtil.getCurrentZonedDateTime().plusDays(1L).toInstant()))
            .sign(Algorithm.HMAC256(JWT_SECRET))


        engine = TestApplicationEngine().apply {
            (environment.config as MapApplicationConfig).apply {
                // Set here the properties
                put("app.jwt.secret", JWT_SECRET)
            }
            start(wait = false)
            application.authModule()
        }
    }

    @Test
    fun testAuthWho() {
        with(engine) {
            handleRequest(HttpMethod.Get, "/auth/who") {
                addHeader("Authorization", "Bearer $authToken")
            }.response.apply {
                assertEquals("Success, userId=$userId", content)
            }
        }
    }

    @Test
    fun testAuthDummyToken() {
        with(engine) {
            handleRequest(HttpMethod.Get, "/auth/dummy-token?userId=$userId").response.apply {
                assertEquals(authToken, content)
                assertEquals(HttpStatusCode.OK, status())
            }
        }
    }
}
