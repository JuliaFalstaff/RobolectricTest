package com.example.robolectrictest.repository

import com.example.robolectrictest.model.SearchResponse
import retrofit2.Response

interface GitHubRepositoryCallback {
    fun handleGitHubResponse(response: Response<SearchResponse?>?)
    fun handleGitHubError()
}

internal interface RepositoryContract {
    fun searchGithub(
        query: String,
        callback: GitHubRepositoryCallback
    )
}