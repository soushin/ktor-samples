package com.github.soushin.ktorsample.model

import io.ktor.auth.Principal
import io.ktor.auth.jwt.JWTPrincipal
import java.lang.IllegalStateException

data class User(val id: String): Principal {
    companion object {
        fun toUser(principal: JWTPrincipal) = User(
            principal.payload.getClaim("id")?.asString() ?: throw IllegalStateException("unauthorized")
        )
    }
}
