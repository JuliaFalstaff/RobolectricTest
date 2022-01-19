package com.example.robolectrictest.repository

import com.example.robolectrictest.model.SearchResponse
import retrofit2.Response

class FakeGitHubRepository: RepositoryContract {
    override fun searchGithub(query: String, callback: GitHubRepositoryCallback) {
        callback.handleGitHubResponse(Response.success(SearchResponse(42, listOf())))
    }
}