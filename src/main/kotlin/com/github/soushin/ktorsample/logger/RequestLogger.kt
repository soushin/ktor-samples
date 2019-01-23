package com.github.soushin.ktorsample.logger

import com.fasterxml.jackson.databind.ObjectMapper

data class RequestLog(
    private val objectMapper: ObjectMapper,
    val remoteAddr: String?,
    val name: String,
    val body: Map<String, String?>,
    val userId: String?,
    val userAgent: String?,
    val method: String?,
    val requestId: String?
) {

    class Builder(
        private val objectMapper: ObjectMapper,
        var name: String = "defaultName",
        var remoteAddr: String? = null,
        var element: Map<String, String?> = mapOf(),
        var userId: String? = null,
        var userAgent: String? = null,
        var method: String? = null,
        var requestId: String? = null
    ) {
        fun build(): RequestLog =
            RequestLog(
                objectMapper = objectMapper,
                name = name,
                remoteAddr = remoteAddr,
                body = element,
                userId = userId,
                userAgent = userAgent,
                method = method,
                requestId = requestId
            )

    }

    override fun toString(): String = try {
        objectMapper.writeValueAsString(this)
    } catch (e: Exception) {
        e.toString()
    }
}
