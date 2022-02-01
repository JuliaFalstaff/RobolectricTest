package com.example.robolectrictest.repository

import com.example.robolectrictest.model.SearchResponse
import com.example.robolectrictest.model.SearchResult
import com.example.robolectrictest.presenter.RepositoryContract
import io.reactivex.Observable
import retrofit2.Response
import kotlin.random.Random

class FakeGitHubRepository: RepositoryContract {
    override fun searchGithub(query: String, callback: GitHubRepositoryCallback) {
        callback.handleGitHubResponse(Response.success(getFakeResponse()))
    }

    override fun searchGithub(query: String): Observable<SearchResponse> {
        return Observable.just(getFakeResponse())
    }

    override suspend fun searchGithubAsync(query: String): SearchResponse {
        return getFakeResponse()
    }

    private fun getFakeResponse(): SearchResponse {
        val list: MutableList<SearchResult> = mutableListOf()
        for (index in 1..100) {
            list.add(
                SearchResult(
                    id = index,
                    name = "Name: $index",
                    fullName = "FullName: $index",
                    private = Random.nextBoolean(),
                    description = "Description: $index",
                    updatedAt = "Updated: $index",
                    size = index,
                    stargazersCount = Random.nextInt(100),
                    language = "",
                    hasWiki = Random.nextBoolean(),
                    archived = Random.nextBoolean(),
                    score = index.toDouble()
                )
            )
        }
        return SearchResponse(list.size, list)
    }
}