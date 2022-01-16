package com.example.robolectrictest.presenter.search

import com.example.robolectrictest.presenter.PresenterContract


internal interface PresenterSearchContract : PresenterContract {
    fun searchGitHub(searchQuery: String)
    //onAttach
    //onDetach
}
