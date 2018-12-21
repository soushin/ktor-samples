package com.github.soushin.ktorsample.exception

import io.ktor.http.HttpStatusCode

abstract class SystemException(message: String, ex: Exception?) : RuntimeException(message, ex) {
    abstract val status: HttpStatusCode
    fun response() = HttpError(code = status.value, message = message ?: "error")
}

class InternalServerErrorException : SystemException {
    constructor(message: String) : super(message, null)
    constructor(message: String, ex: Exception) : super(message, ex)

    override val status: HttpStatusCode = HttpStatusCode.InternalServerError
}

class NotFoundException : SystemException {
    constructor(message: String) : super(message, null)

    override val status: HttpStatusCode = HttpStatusCode.NotFound
}

class BadRequestException : SystemException {
    constructor(message: String) : super(message, null)

    override val status: HttpStatusCode = HttpStatusCode.BadRequest
}

data class HttpError(
    val message: String,
    val code: Int
)
