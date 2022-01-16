package com.example.robolectrictest.presenter.details

import com.example.robolectrictest.view.ViewContract
import com.example.robolectrictest.view.details.ViewDetailsContract


internal class DetailsPresenter internal constructor(
        private val viewContract: ViewDetailsContract,
        private var count: Int = 0
) : PresenterDetailsContract {

    private var view: ViewContract? = null
    fun getView() = view

    override fun setCounter(count: Int) {
        this.count = count
    }

    override fun onIncrement() {
        count++
        viewContract.setCount(count)
    }

    override fun onDecrement() {
        count--
        viewContract.setCount(count)
    }

    override fun onAttach(viewAttach: ViewContract?) {
        if (view != viewAttach) {
            view = viewAttach
        }
    }

    override fun onDetach() {
        view = null
    }
}
