package com.example.robolectrictest.view.search

import com.example.robolectrictest.model.SearchResult
import com.example.robolectrictest.view.ViewContract


internal interface ViewSearchContract : ViewContract {
    fun displaySearchResults(
            searchResults: List<SearchResult>,
            totalCount: Int
    )

    fun displayError()
    fun displayError(error: String)
    fun displayLoading(show: Boolean)
}
