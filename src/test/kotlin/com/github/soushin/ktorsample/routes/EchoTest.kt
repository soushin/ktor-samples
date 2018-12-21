package com.github.soushin.ktorsample.routes

import com.github.soushin.ktorsample.echoModule
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
class EchoTest {

    private lateinit var engine: TestApplicationEngine

    @Before
    fun before() {
        engine = TestApplicationEngine().apply {
            start(wait = false)
            application.echoModule()
        }
    }

    @Test
    fun testEcho() {
        with(engine) {
            handleRequest(HttpMethod.Get, "/echo?q=hello").response.apply {
                assertEquals("hello", content)
                assertEquals(HttpStatusCode.OK, status())
            }
        }
    }
}
