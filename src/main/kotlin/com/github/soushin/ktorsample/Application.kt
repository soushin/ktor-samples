package com.github.soushin.ktorsample

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.SerializationFeature
import com.github.soushin.ktorsample.client.github.GitHubClient
import com.github.soushin.ktorsample.config.Config.gitHubBaseUrl
import com.github.soushin.ktorsample.config.Config.gitHubToken
import com.github.soushin.ktorsample.config.Config.jwtSecret
import com.github.soushin.ktorsample.config.loggerAttributeKey
import com.github.soushin.ktorsample.exception.SystemException
import com.github.soushin.ktorsample.logger.RequestLogBuilder
import com.github.soushin.ktorsample.model.User
import com.github.soushin.ktorsample.repository.github.PullRequestRepository
import com.github.soushin.ktorsample.repository.github.PullRequestRepositoryImpl
import com.github.soushin.ktorsample.repository.github.RepoRepository
import com.github.soushin.ktorsample.repository.github.RepoRepositoryImpl
import com.github.soushin.ktorsample.routes.auth
import com.github.soushin.ktorsample.routes.echo
import com.github.soushin.ktorsample.routes.error
import com.github.soushin.ktorsample.routes.repository
import com.github.soushin.ktorsample.usecase.github.RepositoryUseCase
import com.github.soushin.ktorsample.usecase.github.RepositoryUseCaseImpl
import com.github.soushin.ktorsample.util.DateTimeUtil
import com.github.soushin.ktorsample.util.logBuilder
import com.github.soushin.ktorsample.util.user
import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.jackson.jackson
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.request.header
import io.ktor.request.uri
import io.ktor.request.userAgent
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import mu.KotlinLogging
import org.koin.dsl.module.module
import org.koin.ktor.ext.inject
import org.koin.ktor.ext.installKoin
import org.threeten.bp.Clock
import org.threeten.bp.ZoneId

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.echoModule() {
    val logger = KotlinLogging.logger("RequestLogger")
    install(Locations)
    intercept(ApplicationCallPipeline.Monitoring) {
        call.attributes.put(
            loggerAttributeKey,
            RequestLogBuilder()
                .name(call.request.uri)
                .userAgent(call.request.userAgent())
                .remoteAddr(call.request.header("X-Forwarded-For") ?: "-")
        )
        try {
            proceed()
        } catch (e: Exception) {
            logger.error { call.logBuilder.userId(call.user?.id).build() }
            throw e
        }
        logger.info { call.logBuilder.success(true).userId(call.user?.id).build() }
    }
    routing {
        echo()
    }
}

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.errorModule() {
    install(Locations)
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }
    install(StatusPages) {
        exception<SystemException> { cause ->
            call.response.status(cause.status)
            call.respond(cause.response())
        }
    }
    routing {
        error()
    }
}

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.authModule() {
    install(Locations)
    install(StatusPages) {
        exception<SystemException> { cause ->
            call.response.status(cause.status)
            call.respond(cause.response())
        }
    }
    install(Authentication) {
        jwt {
            verifier(JWT.require(Algorithm.HMAC256(jwtSecret)).build())
            validate { credential ->
                User.toUser(JWTPrincipal(credential.payload))
            }
        }
    }
    routing {
        auth(jwtSecret)
    }
}

@KtorExperimentalAPI
fun Application.diModule() {
    val usecaseModule = module {
        // client
        single { GitHubClient(getProperty("github_base_url"), getProperty("github_auth_token")) }
        // repository
        single<RepoRepository> { RepoRepositoryImpl(get()) }
        single<PullRequestRepository> { PullRequestRepositoryImpl(get()) }
        // useCase
        single<RepositoryUseCase> { RepositoryUseCaseImpl(get(), get()) }
    }
    installKoin(
        listOf(usecaseModule),
        extraProperties = mapOf(
            "github_base_url" to gitHubBaseUrl,
            "github_auth_token" to gitHubToken
        )
    )
}

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.mainModule() {

    val logger = KotlinLogging.logger("RequestLogger")

    install(CallLogging)
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }
    intercept(ApplicationCallPipeline.Monitoring) {
        call.attributes.put(
            loggerAttributeKey,
            RequestLogBuilder()
                .name(call.request.uri)
                .userAgent(call.request.userAgent())
                .remoteAddr(call.request.header("X-Forwarded-For") ?: "-")
        )
        try {
            proceed()
        } catch (e: Exception) {
            logger.error { call.logBuilder.userId(call.user?.id).build() }
            throw e
        }
        logger.info { call.logBuilder.success(true).userId(call.user?.id).build() }
    }
    install(Locations)
    install(StatusPages) {
        exception<SystemException> { cause ->
            call.response.status(cause.status)
            call.respond(cause.response())
        }
    }

    val usecase by inject<RepositoryUseCase>()

    routing {
        repository(usecase)
    }
}

@KtorExperimentalAPI
fun main(args: Array<String>) {
    DateTimeUtil.init(Clock.system(ZoneId.systemDefault()))
    embeddedServer(Netty, commandLineEnvironment(args)).start(wait = true)
}
