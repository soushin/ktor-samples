package com.github.soushin.ktorsample.util

import com.github.soushin.ktorsample.config.loggerAttributeKey
import com.github.soushin.ktorsample.model.User
import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication

val ApplicationCall.user
    get() = this.authentication.principal<User>()

val ApplicationCall.logBuilder
    get() = this.attributes[loggerAttributeKey]
