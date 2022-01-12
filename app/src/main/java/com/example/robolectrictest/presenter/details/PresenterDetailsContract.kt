package com.example.robolectrictest.presenter.details

import com.example.robolectrictest.presenter.PresenterContract


internal interface PresenterDetailsContract : PresenterContract {
    fun setCounter(count: Int)
    fun onIncrement()
    fun onDecrement()
}
