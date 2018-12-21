package com.github.soushin.ktorsample.client.github

import com.github.soushin.ktorsample.client.Client
import com.github.soushin.ktorsample.domain.github.Repository

class GetRepositories(
    type: String,
    perPage: Int
) : Client.Request<List<Repository>>() {
    override val path = "/user/repos"
    override val parameters = listOf("type" to type, "per_page" to perPage, "sort" to "pushed")
}
