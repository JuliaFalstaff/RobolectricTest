package com.example.robolectrictest.presenter.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.robolectrictest.AppState
import com.example.robolectrictest.MainActivity.Companion.BASE_URL
import com.example.robolectrictest.model.SearchResponse
import com.example.robolectrictest.presenter.RepositoryContract
import com.example.robolectrictest.repository.GitHubApi
import com.example.robolectrictest.repository.GitHubRepository
import com.example.robolectrictest.scheduler.ISchedulerProvider
import com.example.robolectrictest.scheduler.SearchSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import org.junit.internal.runners.ErrorReportingRunner
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SearchViewModel(
    private val repository: RepositoryContract = GitHubRepository(
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(GitHubApi::class.java)
    ),
    private val appSchedulerProvider: ISchedulerProvider = SearchSchedulerProvider()
): ViewModel() {
    private val _liveData = MutableLiveData<AppState>()
    private val liveData: LiveData<AppState> = _liveData
    private var compositeDisposable = CompositeDisposable()

    fun subscribeToLiveData() = liveData

    fun searchGitHub(searchQuery: String) {
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            repository.searchGithub(searchQuery)
                .subscribeOn(appSchedulerProvider.io())
                .observeOn(appSchedulerProvider.ui())
                .doOnSubscribe { _liveData.postValue(AppState.Loading) }
                .subscribeWith(object : DisposableObserver<SearchResponse>() {
                    override fun onNext(searchResponse: SearchResponse) {
                        val searchResults = searchResponse.searchResults
                        val totalCount = searchResponse.totalCount
                        if (searchResults != null && totalCount != null) {
                            _liveData.postValue(AppState.Working(searchResponse))
                        } else {
                            _liveData.postValue(AppState.Error(Throwable(ERROR)))
                        }
                    }

                    override fun onError(e: Throwable) {
                        _liveData.postValue(AppState.Error(Throwable(e.message ?: ERROR)))
                    }

                    override fun onComplete() {}
                })
        )
    }

    companion object {
        private const val ERROR = "Search results or total count are null"
    }
}