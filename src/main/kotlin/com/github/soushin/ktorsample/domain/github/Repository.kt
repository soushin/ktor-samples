package com.github.soushin.ktorsample.domain.github

import com.fasterxml.jackson.annotation.JsonProperty

data class Repository(
    val url: String,
    val name: String,
    @JsonProperty("full_name")
    val fullName: String,
    val owner: Owner
)

data class Owner(
    val login: String
)
