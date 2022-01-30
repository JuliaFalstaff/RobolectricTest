package com.example.robolectrictest

import com.example.robolectrictest.model.SearchResponse

sealed class AppState {
    object Loading: AppState()
    data class Working(val searchResponse: SearchResponse) : AppState()
    data class Error(val error: Throwable) : AppState()
}