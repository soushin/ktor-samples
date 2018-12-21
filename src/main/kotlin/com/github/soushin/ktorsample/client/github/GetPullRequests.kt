package com.github.soushin.ktorsample.client.github

import com.github.soushin.ktorsample.client.Client
import com.github.soushin.ktorsample.domain.github.PullRequest

class GetPullRequests(
    owner: String,
    repo: String,
    state: String,
    perPage: Int
) : Client.Request<List<PullRequest>>() {
    override val path = "/repos/$owner/$repo/pulls"
    override val parameters = listOf("state" to state, "per_page" to perPage)
}
