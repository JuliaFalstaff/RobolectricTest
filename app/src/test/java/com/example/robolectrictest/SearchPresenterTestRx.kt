package com.example.robolectrictest

import com.example.robolectrictest.model.SearchResponse
import com.example.robolectrictest.presenter.search.SearchPresenter
import com.example.robolectrictest.repository.GitHubRepository
import com.example.robolectrictest.stubs.SchedulerProviderStub
import com.example.robolectrictest.view.search.ViewSearchContract
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class SearchPresenterTestRx {
    private lateinit var presenter: SearchPresenter

    @Mock
    private lateinit var repository: GitHubRepository

    @Mock
    private lateinit var viewContract: ViewSearchContract

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        presenter = SearchPresenter(viewContract, repository, SchedulerProviderStub())
    }

    //Проверим вызов метода searchGitHub() у нашего Репозитория и что метод вызвался 1 раз
    @Test
    fun searchGitHub_Test() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(SearchResponse(1, listOf()))
        )
        presenter.searchGitHub(SEARCH_QUERY)
        verify(repository, times(1)).searchGithub(SEARCH_QUERY)
    }

    @Test //Проверяем как обрабатывается ошибка запроса и убеждаемся, что в этом случае вызывается метод viewContract’а displayError.
    fun handleRequestError_Test() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.error(
                Throwable(
                    ERROR_TEXT
                )
            )
        )
        presenter.searchGitHub(SEARCH_QUERY)
        verify(viewContract, times(1)).displayError("error")
    }

    //Проверим порядок вызова методов при ошибке.
    @Test
    fun handleResponseError_TotalCountIsNull_ViewContractMethodOrder() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(SearchResponse(null, listOf()))
        )
        presenter.searchGitHub(SEARCH_QUERY)
        val inOrder = inOrder(viewContract)
        inOrder.verify(viewContract).displayLoading(true)
        inOrder.verify(viewContract).displayError("Search results or total count are null")
        inOrder.verify(viewContract).displayLoading(false)
    }

    //Теперь проверим успешный ответ сервера
    @Test
    fun handleResponseSuccess() {
        `when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(SearchResponse(TEST_FAKE_NUMBER_42, listOf()))
        )
        presenter.searchGitHub(SEARCH_QUERY)
        verify(viewContract, times(1)).displaySearchResults(listOf(), TEST_FAKE_NUMBER_42)
    }

    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "error"
    }
}