package com.github.soushin.ktorsample.config

import com.github.soushin.ktorsample.logger.RequestLogBuilder
import io.ktor.application.Application
import io.ktor.util.AttributeKey
import io.ktor.util.KtorExperimentalAPI


@KtorExperimentalAPI
object Config {

    val Application.gitHubBaseUrl
        get() = environment.config.property("app.github.baseUrl").getString()

    val Application.gitHubToken
        get() = environment.config.property("app.github.token").getString()

    val Application.jwtSecret
        get() = environment.config.property("app.jwt.secret").getString()
}

val loggerAttributeKey = AttributeKey<RequestLogBuilder>("loggerAttributeKey")
