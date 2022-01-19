package com.example.robolectrictest.repository

import com.example.robolectrictest.model.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class GitHubRepository(private val gitHubApi: GitHubApi)  : RepositoryContract {

    override fun searchGithub(
        query: String,
        callback: GitHubRepositoryCallback
    ) {
        val call = gitHubApi.searchGithub(query)
        call?.enqueue(object : Callback<SearchResponse?> {

            override fun onResponse(
                    call: Call<SearchResponse?>,
                    response: Response<SearchResponse?>
            ) {
                callback.handleGitHubResponse(response)
            }

            override fun onFailure(
                    call: Call<SearchResponse?>,
                    t: Throwable
            ) {
                callback.handleGitHubError()
            }
        })
    }
}
