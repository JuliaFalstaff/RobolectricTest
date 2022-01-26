package com.example.robolectrictest

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.robolectrictest.view.details.DetailsFragment
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsFragmentEspressoTest {
    private lateinit var scenario: FragmentScenario<DetailsFragment>

    @Before
    fun setUp() {
        //Запускаем Fragment в корне Activity
        scenario = launchFragmentInContainer()
    }

    @Test
    fun fragment_testAssertNotNull() {
        scenario.onFragment {
            TestCase.assertNotNull(it)
        }
    }

    @Test
    fun fragment_testBundle() {
        //Можно передавать аргументы во Фрагмент, но это необязательно
        val fragmentArgs = bundleOf("TOTAL_COUNT_EXTRA" to TEST_FAKE_NUMBER_42)
        //Создаем новый сценарий с аргументами и Запускаем Fragment с аргументами
        val scenario = launchFragmentInContainer<DetailsFragment>(fragmentArgs)
        //Возможность менять стейт Фрагмента
        scenario.moveToState(Lifecycle.State.RESUMED)

        val assertion: ViewAssertion = ViewAssertions.matches(withText(TEST_NUMBER_OF_RESULTS_42))
        onView(withId(R.id.totalCountTextView)).check(assertion)
    }

    @Test
    fun fragment_testSetCountMethod() {
        scenario.onFragment {
            it.setCount(TEST_FAKE_NUMBER_42)
        }
        val assertion = matches(withText(TEST_NUMBER_OF_RESULTS_42))
        onView(withId(R.id.totalCountTextView)).check(assertion)
    }

    @Test
    fun fragment_testIncrementButton() {
        onView(withId(R.id.incrementButton)).perform(click())
        onView(withId(R.id.totalCountTextView)).check(matches(withText(TEST_NUMBER_OF_RESULTS_PLUS_1)))
    }

    @Test
    fun fragment_testDecrementButton() {
        onView(withId(R.id.decrementButton)).perform(click())
        onView(withId(R.id.totalCountTextView)).check(
            matches(
                withText(
                    TEST_NUMBER_OF_RESULTS_MINUS_1
                )
            )
        )
    }

    @Test
    fun fragment_buttonsAreEffectiveVisible() {
        onView(withId(R.id.incrementButton)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.decrementButton)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun fragment_textViewIsCompletelyVisible() {
        onView(withId(R.id.totalCountTextView)).check(matches(isCompletelyDisplayed()))
    }

    @After
    fun close() {
        scenario.close()
    }
}