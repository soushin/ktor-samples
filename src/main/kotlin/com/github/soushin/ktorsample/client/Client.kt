package com.github.soushin.ktorsample.client

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.soushin.ktorsample.exception.InternalServerErrorException

abstract class Client {

    abstract class Request<T> {
        abstract val path: String
        open val parameters = emptyList<Pair<String, Any?>>()
        open val headers = emptyMap<String, Any>()
        inline fun <reified T : Any> parse(response: String) = try {
            jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue<T>(response)
        } catch (e: Exception) {
            throw InternalServerErrorException("parse error, msg:${e.message}", e)
        }
    }

    abstract val client: FuelManager
    abstract val baseUrl: String
    abstract val token: String

    inline fun <reified T : Any> get(request: Request<T>): T {
        val (_, res, result) = client.request(Method.GET, "$baseUrl${request.path}", request.parameters)
            .header(request.headers.plus("Authorization" to "token $token"))
            .responseString()
        if (!res.isSuccessful) error(res)
        return request.parse<T>(result.get())
    }

    abstract fun error(response: Response)
}
