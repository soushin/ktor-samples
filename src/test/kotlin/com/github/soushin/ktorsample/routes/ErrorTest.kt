package com.github.soushin.ktorsample.routes

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.soushin.ktorsample.errorModule
import com.github.soushin.ktorsample.exception.HttpError
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.util.KtorExperimentalAPI
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@KtorExperimentalLocationsAPI
@KtorExperimentalAPI
class ErrorTest {

    private lateinit var engine: TestApplicationEngine
    private val objectMapper = ObjectMapper().registerKotlinModule()

    @Before
    fun before() {
        engine = TestApplicationEngine().apply {
            start(wait = false)
            application.errorModule()
        }
    }

    @Test
    fun testError() {
        with(engine) {
            handleRequest(HttpMethod.Get, "/errors/404").response.apply {
                objectMapper.readValue<HttpError>(content!!).let {
                    assertEquals("not found", it.message)
                    assertEquals(HttpStatusCode.NotFound.value, it.code)
                }
                assertEquals(HttpStatusCode.NotFound, status())
            }

            handleRequest(HttpMethod.Get, "/errors/400").response.apply {
                objectMapper.readValue<HttpError>(content!!).let {
                    assertEquals("bad request", it.message)
                    assertEquals(HttpStatusCode.BadRequest.value, it.code)
                }
                assertEquals(HttpStatusCode.BadRequest, status())
            }

            handleRequest(HttpMethod.Get, "/errors/500").response.apply {
                objectMapper.readValue<HttpError>(content!!).let {
                    assertEquals("internal server error", it.message)
                    assertEquals(HttpStatusCode.InternalServerError.value, it.code)
                }
                assertEquals(HttpStatusCode.InternalServerError, status())
            }
        }
    }
}
