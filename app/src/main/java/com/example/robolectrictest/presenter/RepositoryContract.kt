package com.example.robolectrictest.presenter

import com.example.robolectrictest.model.SearchResponse
import com.example.robolectrictest.repository.GitHubRepositoryCallback
import io.reactivex.Observable

interface RepositoryContract {
    fun searchGithub(
        query: String,
        callback: GitHubRepositoryCallback
    )

    //мы не передаем в новый метод никаких колбеков, так как мы работаем с потоком
    fun searchGithub(
        query: String
    ): Observable<SearchResponse>

    suspend fun searchGithubAsync(query: String): SearchResponse
}