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
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class SearchViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var searchViewModel: SearchViewModel

    @Mock
    private lateinit var repository: FakeGitHubRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchViewModel = SearchViewModel(repository, SchedulerProviderStub())
    }

    @Test //Проверим вызов метода searchGitHub() у нашей ВьюМодели
    fun search_Test() {
        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(SearchResponse(1, listOf()))
        )
        searchViewModel.searchGitHub(SEARCH_QUERY)
        verify(repository, times(1)).searchGithub(SEARCH_QUERY)
    }

    @Test //Проверим вызов Error метода searchGitHub() у нашей ВьюМодели
    fun search_TestReturnError() {
        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(Observable.error(Throwable(ERROR_TEXT)))
        searchViewModel.searchGitHub(SEARCH_QUERY)
        verify(repository, times(1)).searchGithub(SEARCH_QUERY)
    }

    @Test
    fun liveData_TestReturnValueIsNotNull() {
        //Создаем обсервер. В лямбде мы не вызываем никакие методы - в этом нет необходимости
        //так как мы проверяем работу LiveData и не собираемся ничего делать с данными, которые она возвращает
        val observer = Observer<AppState> {}
        //Получаем LiveData
        val liveData = searchViewModel.subscribeToLiveData()

        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
            Observable.just(SearchResponse(1, listOf()))
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

    @Test
    fun liveData_TestReturnValueIsError() {
        val observer = Observer<AppState>{}
        val liveData = searchViewModel.subscribeToLiveData()
        val error = Throwable(ERROR_TEXT)
        //При вызове Репозитория возвращаем ошибку
        Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(Observable.error(error))
        try {
            liveData.observeForever(observer)
            searchViewModel.searchGitHub(SEARCH_QUERY)
            //Убеждаемся, что Репозиторий вернул ошибку и LiveData возвращает ошибку
            val value: AppState.Error = liveData.value as AppState.Error
            Assert.assertEquals(value.error.message, error.message)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "error"
    }
}