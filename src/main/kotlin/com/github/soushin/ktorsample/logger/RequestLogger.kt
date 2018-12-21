package com.github.soushin.ktorsample.logger

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.ZonedDateTime

data class RequestLog(
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val date: ZonedDateTime,
    val remoteAddr: String?,
    val name: String,
    val success: Boolean,
    val body: Map<String, String?>,
    val userId: String?,
    val userAgent: String?
)

class RequestLogBuilder() {

    private var nameHolder: String = "defaultName"
    private var remoteAddrHolder: String? = null
    private var successHolder: Boolean = false
    private var elementHolder: Map<String, String?> = mapOf()
    private var userIdHolder: String? = null
    private var userAgentHolder: String? = null

    fun name(name: String) = apply {
        nameHolder = name
    }

    fun remoteAddr(remoteAddr: String?) = apply {
        remoteAddrHolder = remoteAddr
    }

    fun success(success: Boolean) = apply {
        successHolder = success
    }

    fun elem(element: Pair<String, String?>) = apply {
        elementHolder = elementHolder.plus(element)
    }

    fun userId(userId: String?) = apply {
        userIdHolder = userId
    }

    fun userAgent(userAgent: String?) = apply {
        userAgentHolder = userAgent
    }

    fun build(): RequestLog {
        val log = RequestLog(
            date = ZonedDateTime.now(),
            name = nameHolder,
            remoteAddr = remoteAddrHolder,
            success = successHolder,
            body = elementHolder,
            userId = userIdHolder,
            userAgent = userAgentHolder
        )
        return log
    }
}
