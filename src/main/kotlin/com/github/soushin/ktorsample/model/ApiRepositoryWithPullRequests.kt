package com.github.soushin.ktorsample.model

data class ApiRepositoryWithPullRequests(
    val name: String,
    val openPullRequests: List<ApiPullRequest>,
    val closePullRequests: List<ApiPullRequest>
)

data class ApiPullRequest(val number: String, val title: String)
