package com.example.robolectrictest.presenter

import com.example.robolectrictest.view.ViewContract

internal interface PresenterContract {
    fun onAttach(view: ViewContract)
    fun onDetach()
}
