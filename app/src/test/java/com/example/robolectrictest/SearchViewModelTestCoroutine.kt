package com.example.robolectrictest

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.robolectrictest.model.SearchResponse
import com.example.robolectrictest.presenter.search.SearchViewModel
import com.example.robolectrictest.presenter.stubs.SchedulerProviderStub
import com.example.robolectrictest.repository.FakeGitHubRepository
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class SearchViewModelTestCoroutine {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    private lateinit var searchViewModel: SearchViewModel

    @Mock
    private lateinit var repository: FakeGitHubRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchViewModel = SearchViewModel(repository, SchedulerProviderStub())
    }

    @Test
    fun coroutines_TestReturnValueIsNotNull() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<AppState> {}
            //Получаем LiveData
            val liveData = searchViewModel.subscribeToLiveData()

            Mockito.`when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                    SearchResponse(1, listOf())
            )
            try {
                //Подписываемся на LiveData без учета жизненного цикла
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)
                //Убеждаемся, что Репозиторий вернул данные и LiveData передала их Наблюдателям
                Assert.assertNotNull(liveData.value)
            } finally {
                //Тест закончен, снимаем Наблюдателя
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun coroutines_TestReturnValueIsError() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<AppState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            Mockito.`when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                    SearchResponse(null, listOf())
            )

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)

                val value: AppState.Error = liveData.value as AppState.Error
                Assert.assertEquals(value.error.message, "Search results or total count are null")
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun coroutines_TestException() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<AppState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)

                val value: AppState.Error = liveData.value as AppState.Error
                Assert.assertEquals(value.error.message, "Search results or total count are null")
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    @Test
    fun coroutine_SuccessTest(){
        testCoroutineRule.runBlockingTest {
            val observer = Observer<AppState> {}
            val liveData = searchViewModel.subscribeToLiveData()
            Mockito.`when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                    SearchResponse(
                            1,
                            listOf()
                    )
            )
            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)
                val value = liveData.value as AppState.Working
                Assert.assertEquals(value.searchResponse, SearchResponse(1, listOf()))
            } finally {
                liveData.removeObserver(observer)
            }

        }
    }

    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "error"
        private const val EXCEPTION_TEXT = "Response is null or unsuccessful"
    }
}