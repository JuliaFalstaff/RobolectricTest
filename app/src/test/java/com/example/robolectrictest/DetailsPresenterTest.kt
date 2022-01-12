package com.example.robolectrictest

import com.example.robolectrictest.presenter.details.DetailsPresenter
import com.example.robolectrictest.view.ViewContract
import com.example.robolectrictest.view.details.ViewDetailsContract
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DetailsPresenterTest {

    private lateinit var presenter: DetailsPresenter

    private var view: ViewContract? = null

    @Mock
    private lateinit var viewContract: ViewDetailsContract

    private var count: Int = 0

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        presenter = DetailsPresenter(viewContract, count)
    }

    @Test
    fun setCounter_TestCountEquals() {
        presenter.setCounter(count)
        assertEquals(0, count)
    }

    @Test
    fun onIncrement_TestIsIncrementing() {
        presenter.onIncrement()
        verify(viewContract, times(1)).setCount(1)
    }

    @Test
    fun onDecrement_TestIsDecrementing() {
        presenter.onDecrement()
        verify(viewContract, times(1)).setCount(-1)
    }

    @Test
    fun onAttach_Test_NotSameView() {
        presenter.onAttach(view)
        assertNotSame(viewContract, view)
    }

    @Test
    fun onAttach_Test_Attached() {
        presenter.onAttach(view)
        view = viewContract
        assertEquals(viewContract, view)
    }

    @Test
    fun onAttach_Test_ViewNotNull() {
        presenter.onAttach(view)
        view = viewContract
        assertNotNull(view)
    }

    @Test
    fun onDetach_Test() {
        presenter.onAttach(view)
        view = viewContract
        presenter.onDetach()
        view = null
        assertNull(view)
            }
}