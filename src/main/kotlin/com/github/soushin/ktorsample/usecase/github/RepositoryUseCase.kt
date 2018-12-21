package com.github.soushin.ktorsample.usecase.github

import com.github.soushin.ktorsample.domain.github.PullRequest
import com.github.soushin.ktorsample.model.ApiPullRequest
import com.github.soushin.ktorsample.model.ApiRepositoryWithPullRequests
import com.github.soushin.ktorsample.repository.github.PullRequestRepository
import com.github.soushin.ktorsample.repository.github.RepoRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

interface RepositoryUseCase {
    fun getRepositoryWithPullRequests(type: String, perPage: Int): List<ApiRepositoryWithPullRequests>
}

class RepositoryUseCaseImpl(
    private val repoRepository: RepoRepository,
    private val pullRequestRepository: PullRequestRepository
) : RepositoryUseCase {

    override fun getRepositoryWithPullRequests(type: String, perPage: Int) = runBlocking {
        repoRepository.getRepositories(type, perPage).map {
            val open = GlobalScope.async {
                pullRequestRepository.getPullRequests(it.owner.login, it.name, "open", 3).map { it.toApi() }
            }
            val close = GlobalScope.async {
                pullRequestRepository.getPullRequests(it.owner.login, it.name, "close", 3).map { it.toApi() }
            }
            ApiRepositoryWithPullRequests(it.name, open.await(), close.await())
        }
    }
}

fun PullRequest.toApi() = ApiPullRequest(number, title)
