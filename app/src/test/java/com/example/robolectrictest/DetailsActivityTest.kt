package com.example.robolectrictest

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.robolectrictest.view.details.DetailsActivity
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class DetailsActivityTest {

    lateinit var scenario: ActivityScenario<DetailsActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(DetailsActivity::class.java)
    }

    //Проверка, что Activity действительно создается
    @Test
    fun activity_AssertNotNull() {
        scenario.onActivity {
            assertNotNull(it)
        }
    }

    //Проверка, что активити в нужном состоянии
    @Test
    fun activity_IsResumed() {
        assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    //Проверка. что загружается нужный layout
    @Test
    fun activityTextView_NotNull() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            assertNotNull(totalCountTextView)
        }
    }

    @Test
    fun activityTextView_HasText() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            assertEquals("Number of results: 0", totalCountTextView.text)
        }
    }

    @Test
    fun activityTextView_IsVisible() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            assertEquals(View.VISIBLE, totalCountTextView.visibility)
        }
    }

    @Test
    fun activityButtons_AreVisible() {
        scenario.onActivity {
            val decrementButton = it.findViewById<Button>(R.id.decrementButton)
            assertEquals(View.VISIBLE, decrementButton.visibility)
            val incrementButton = it.findViewById<Button>(R.id.incrementButton)
            assertEquals(View.VISIBLE, incrementButton.visibility)
        }
    }

    @Test
    fun activityButtonIncrement_IsWorking() {
        scenario.onActivity {
            val incrementButton = it.findViewById<Button>(R.id.incrementButton)
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            incrementButton.performClick()
            assertEquals("Number of results: 1", totalCountTextView.text)
        }
    }

    @Test
    fun activityButtonDecrement_IsWorking() {
        scenario.onActivity {
            val decrementButton = it.findViewById<Button>(R.id.decrementButton)
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            decrementButton.performClick()
            assertEquals("Number of results: -1", totalCountTextView.text)
        }
    }

    @Test
    fun activityCreateIntent_NotNull() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val intent = DetailsActivity.getIntent(context, 0)
        assertNotNull(intent)
    }


    @Test
    fun activityCreateIntent_HasExtras() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val intent = DetailsActivity.getIntent(context, 0)
        val bundle = intent.extras
        assertNotNull(bundle)
    }

    @Test
    fun activityCreateIntent_HasCount() {
        val count = 42
        val context: Context = ApplicationProvider.getApplicationContext()
        val intent = DetailsActivity.getIntent(context, count)
        val bundle = intent.extras
        assertEquals(count, bundle?.getInt(DetailsActivity.TOTAL_COUNT_EXTRA, 0))
    }

        @After
    fun close() {
        scenario.close()
    }
}