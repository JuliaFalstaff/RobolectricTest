package com.example.robolectrictest.repository

import com.example.robolectrictest.model.SearchResponse
import io.reactivex.Observable
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

    //мы не передаем в новый метод никаких колбеков, так как мы работаем с потоком
    fun searchGithub(
        query: String
    ): Observable<SearchResponse>
}
