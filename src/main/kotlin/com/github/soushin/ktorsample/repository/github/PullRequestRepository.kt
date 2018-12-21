package com.github.soushin.ktorsample.repository.github

import com.github.soushin.ktorsample.client.github.GetPullRequests
import com.github.soushin.ktorsample.client.github.GitHubClient
import com.github.soushin.ktorsample.domain.github.PullRequest

interface PullRequestRepository {
    fun getPullRequests(owner: String, repo: String, state: String, perPage: Int):
            List<PullRequest>
}

class PullRequestRepositoryImpl(private val client: GitHubClient) : PullRequestRepository {
    override fun getPullRequests(owner: String, repo: String, state: String, perPage: Int) =
        client.get(GetPullRequests(owner, repo, state, perPage))
}
