package com.example.robolectrictest.repository

import com.example.robolectrictest.model.SearchResponse
import io.reactivex.Observable
import retrofit2.Response

interface GitHubRepositoryCallback {
    fun handleGitHubResponse(response: Response<SearchResponse?>?)
    fun handleGitHubError()
}
