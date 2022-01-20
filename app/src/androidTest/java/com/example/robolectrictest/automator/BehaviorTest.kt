package com.example.robolectrictest.automator

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class BehaviorTest {
    private val uiDevice: UiDevice = UiDevice.getInstance(getInstrumentation())
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val packageName = context.packageName

    @Before
    fun setup() {
        //Для начала сворачиваем все приложения, если у нас что-то запущено
        uiDevice.pressHome()

        //Запускаем наше приложение
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        //Мы уже проверяли Интент на null в предыдущем тесте, поэтому допускаем, что Интент у нас не null
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

        //Ждем, когда приложение откроется на смартфоне чтобы начать тестировать его элементы
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
    }

    //Убеждаемся, что приложение открыто. Для этого достаточно найти на экране любой элемент
    //и проверить его на null
    @Test
    fun test_MainActivityIsStarted() {
        //Через uiDevice находим editText
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        Assert.assertNotNull(editText)
    }

    //Убеждаемся, что поиск работает как ожидается
    @Test
    fun test_SearchIsPositive() {
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        //Устанавливаем значение
        editText.text = "UiAutomator"
        val searchButton = uiDevice.findObject(By.res(packageName, "searchButton"))
        searchButton.click()

        //Ожидаем конкретного события: появления текстового поля totalCountTextView.
        //Это будет означать, что сервер вернул ответ с какими-то данными, то есть запрос отработал.
        val changedText =
            uiDevice.wait(Until.findObject(By.res(packageName, "totalCountTextView")), TIMEOUT)
        //Убеждаемся, что сервер вернул корректный результат. Обратите внимание, что количество
        //результатов может варьироваться во времени, потому что количество репозиториев постоянно меняется.
        Assert.assertEquals(changedText.text.toString(), "Number of results: 696")
    }

    @Test
    fun test_ButtonsMainActivity_NotNull() {
        val searchButton = uiDevice.findObject(By.res(packageName, "searchButton"))
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        Assert.assertNotNull(searchButton)
        Assert.assertNotNull(toDetails)
    }

    @Test
    fun test_OpenDetailsScreen() {
        //Находим кнопку
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        //Кликаем на кнопку
        toDetails.click()

        //Ожидаем конкретного события: появления текстового поля totalCountTextView.
        //Это будет означать, что DetailsScreen открылся и это поле видно на экране.
        val changedText =
            uiDevice.wait(Until.findObject(By.res(packageName, "totalCountTextView")), TIMEOUT)

        //Убеждаемся, что поле видно и содержит предполагаемый текст.
        //Обратите внимание, что текст должен быть "Number of results: 0",
        //так как мы кликаем по кнопке не отправляя никаких поисковых запросов.
        //Чтобы проверить отображение определенного количества репозиториев,
        //вам в одном и том же методе нужно отправить запрос на сервер и открыть DetailsScreen.
        Assert.assertEquals(changedText.text, "Number of results: 0")
    }

    @Test
    fun test_OpenDetailsScreenWithData() {
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        editText.text = "UiAutomator"
        val searchButton = uiDevice.findObject(By.res(packageName, "searchButton"))
        searchButton.click()
        val changedText =
            uiDevice.wait(Until.findObject(By.res(packageName, "totalCountTextView")), TIMEOUT)
        Assert.assertEquals(changedText.text, "Number of results: 696")
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        toDetails.clickAndWait(Until.newWindow(), TIMEOUT)
        val detailsActivityText =
            uiDevice.findObject(By.res(packageName, "totalCountTextView"))
        Assert.assertEquals(detailsActivityText.text, "Number of results: 696")
    }

    @Test
    fun test_ButtonIsIncrementing() {
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        toDetails.clickAndWait(Until.newWindow(), TIMEOUT)
        val detailsActivityText =
            uiDevice.findObject(By.res(packageName, "totalCountTextView"))
        Assert.assertEquals(detailsActivityText.text, "Number of results: 0")

        val toIncrementingButton = uiDevice.findObject(By.res(packageName, "incrementButton"))
        toIncrementingButton.click()
        val changedTextView = uiDevice.wait(Until.findObject(By.res(packageName, "totalCountTextView")), TIMEOUT)
        Assert.assertEquals(changedTextView.text.toString(), "Number of results: 1")
    }

    @Test
    fun test_ButtonIsDecrementing() {
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        toDetails.clickAndWait(Until.newWindow(), TIMEOUT)
        val detailsActivityText =
            uiDevice.findObject(By.res(packageName, "totalCountTextView"))
        Assert.assertEquals(detailsActivityText.text, "Number of results: 0")

        val toDecrementingButton = uiDevice.findObject(By.res(packageName, "decrementButton"))
        toDecrementingButton.click()
        val changedTextView = uiDevice.wait(Until.findObject(By.res(packageName, "totalCountTextView")), TIMEOUT)
        Assert.assertEquals(changedTextView.text.toString(), "Number of results: -1")
    }

    @Test
    fun test_ButtonsDetailsActivity_NotNull() {
        val toDetails = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        toDetails.clickAndWait(Until.newWindow(), TIMEOUT)
        val detailsActivityText =
            uiDevice.findObject(By.res(packageName, "totalCountTextView"))
        val toDecrementingButton = uiDevice.findObject(By.res(packageName, "decrementButton"))
        val toIncrementingButton = uiDevice.findObject(By.res(packageName, "incrementButton"))
        Assert.assertNotNull(toIncrementingButton)
        Assert.assertNotNull(toDecrementingButton)
        Assert.assertNotNull(detailsActivityText)
    }

    companion object {
        private const val TIMEOUT = 10000L
    }
}