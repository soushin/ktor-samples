package com.github.soushin.ktorsample.repository.github

import com.github.soushin.ktorsample.client.github.GetRepositories
import com.github.soushin.ktorsample.client.github.GitHubClient
import com.github.soushin.ktorsample.domain.github.Repository

interface RepoRepository {
    fun getRepositories(type: String, perPage: Int): List<Repository>
}

class RepoRepositoryImpl(private val client: GitHubClient) : RepoRepository {
    override fun getRepositories(type: String, perPage: Int) = client.get(GetRepositories(type, perPage))
}
