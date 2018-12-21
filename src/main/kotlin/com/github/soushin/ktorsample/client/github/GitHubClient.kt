package com.github.soushin.ktorsample.client.github

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.isClientError
import com.github.soushin.ktorsample.client.Client
import com.github.soushin.ktorsample.exception.BadRequestException
import com.github.soushin.ktorsample.exception.InternalServerErrorException

class GitHubClient(
    override val baseUrl: String,
    override val token: String,
    override val client: FuelManager = FuelManager.instance
) : Client() {
    override fun error(response: Response) {
        when {
            response.isClientError -> throw BadRequestException("GitHubClient error, ${response.responseMessage}")
            else -> throw InternalServerErrorException("GitHubClient error, ${response.responseMessage}")
        }
    }
}
