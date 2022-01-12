package com.example.robolectrictest

import com.example.robolectrictest.presenter.search.SearchPresenter
import com.example.robolectrictest.repository.GitHubRepository
import com.example.robolectrictest.view.ViewContract
import com.example.robolectrictest.view.search.ViewSearchContract
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SearchPresenterTest {
    private lateinit var presenter: SearchPresenter

    private var view: ViewContract? = null

    @Mock
    private lateinit var repository: GitHubRepository

    @Mock
    private lateinit var viewContract: ViewSearchContract

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        presenter = SearchPresenter(viewContract, repository)
    }

    @Test
    fun searchGitHub_Test() {
        val searchQuery = "some query"
        presenter.searchGitHub("some query")
        verify(repository, times(1)).searchGithub(searchQuery, presenter)
    }

    @Test
    fun handleGitHubError_Test() {
        presenter.handleGitHubError()
        Mockito.verify(viewContract, Mockito.times(1)).displayError()
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