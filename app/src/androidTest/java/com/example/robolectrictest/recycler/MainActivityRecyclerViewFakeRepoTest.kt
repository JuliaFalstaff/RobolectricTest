package com.example.robolectrictest.recycler

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.robolectrictest.MainActivity
import com.example.robolectrictest.R
import com.example.robolectrictest.TEST_FAKE_NUMBER_42
import com.example.robolectrictest.view.search.SearchResultAdapter
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityRecyclerViewFakeRepoTest {
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun activitySearch_ScrollTo() {
        loadList()

        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.scrollTo<SearchResultAdapter.SearchResultViewHolder>(
                    hasDescendant(withText("FullName: 42"))
                )
            )
    }

    @Test
    fun activitySearch_PerformClickAtPosition() {
        loadList()

        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<SearchResultAdapter.SearchResultViewHolder>(
                TEST_FAKE_NUMBER_42, click()
            )
        )
    }

    @Test
    fun activitySearch_PerformClickOnItem() {
        loadList()

        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.scrollTo<SearchResultAdapter.SearchResultViewHolder>(
                    hasDescendant(withText("FullName: 65"))
                )
            )

        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItem<SearchResultAdapter.SearchResultViewHolder>(
                    hasDescendant(withText("FullName: 52")), click()
                )
            )
    }

    @Test
    fun activitySearch_PerformCustomClick() {
        loadList()

        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<SearchResultAdapter.SearchResultViewHolder>(
                        0,
                        tapOnItemWithId(R.id.checkbox)
                    )
            )
    }


    @After
    fun close() {
        scenario.close()
    }

    private fun loadList() {
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText("algol"), closeSoftKeyboard())
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())
    }

    private fun tapOnItemWithId(id: Int) = object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Нажимаем на view с указанным id"
        }

        override fun perform(uiController: UiController?, view: View?) {
            val v = view?.findViewById(id) as View
            v.performClick()
        }
    }
}