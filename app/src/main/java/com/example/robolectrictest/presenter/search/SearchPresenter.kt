package com.example.robolectrictest.presenter.search

import com.example.robolectrictest.model.SearchResponse
import com.example.robolectrictest.repository.GitHubRepository
import com.example.robolectrictest.repository.GitHubRepositoryCallback
import com.example.robolectrictest.repository.RepositoryContract
import com.example.robolectrictest.scheduler.ISchedulerProvider
import com.example.robolectrictest.scheduler.SearchSchedulerProvider
import com.example.robolectrictest.view.ViewContract
import com.example.robolectrictest.view.search.ViewSearchContract
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import retrofit2.Response

/**
 * В архитектуре MVP все запросы на получение данных адресуются в Репозиторий.
 * Запросы могут проходить через Interactor или UseCase, использовать источники
 * данных (DataSource), но суть от этого не меняется.
 * Непосредственно Презентер отвечает за управление потоками запросов и ответов,
 * выступая в роли регулировщика движения на перекрестке.
 */

internal class SearchPresenter internal constructor(
    private val viewContract: ViewSearchContract,
    private val repository: RepositoryContract,
    private val appSchedulerProvider: ISchedulerProvider = SearchSchedulerProvider()
) : PresenterSearchContract, GitHubRepositoryCallback {

    private var view: ViewContract? = null
    private var compositeDisposable = CompositeDisposable()

    fun getView() = view

    override fun searchGitHub(searchQuery: String) {
        //Dispose
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            repository.searchGithub(searchQuery)
                .subscribeOn(appSchedulerProvider.io())
                .observeOn(appSchedulerProvider.ui())
                .doOnSubscribe { viewContract.displayLoading(true) }
                .doOnTerminate { viewContract.displayLoading(false) }
                .subscribeWith(object : DisposableObserver<SearchResponse>() {
                    override fun onNext(searchResponse: SearchResponse) {
                        val searchResults = searchResponse.searchResults
                        val totalCount = searchResponse.totalCount
                        if (searchResults!= null && totalCount != null) {
                            viewContract.displaySearchResults(searchResults, totalCount)
                        } else {
                            viewContract.displayError(ERROR)
                        }
                    }

                    override fun onError(e: Throwable) {
                        viewContract.displayError(e.message ?: ERROR)
                    }

                    override fun onComplete() { }

                })
        )

        repository.searchGithub(searchQuery, this)
    }

    override fun onAttach(view: ViewContract?) {
        if (this.view != view) {
            this.view = view
        }
    }

    override fun onDetach() {
        view = null
        compositeDisposable.clear()
    }

    override fun handleGitHubResponse(response: Response<SearchResponse?>?) {
        viewContract.displayLoading(false)
        if (response != null && response.isSuccessful) {
            val searchResponse = response.body()
            val searchResults = searchResponse?.searchResults
            val totalCount = searchResponse?.totalCount
            if (searchResults != null && totalCount != null) {
                viewContract.displaySearchResults(
                        searchResults,
                        totalCount
                )
            } else {
                viewContract.displayError(ERROR)
            }
        } else {
            viewContract.displayError(ERROR)
        }
    }

    override fun handleGitHubError() {
        viewContract.displayLoading(false)
        viewContract.displayError()
    }

    companion object {
        private const val ERROR = "Search results or total count are null"
    }
}
