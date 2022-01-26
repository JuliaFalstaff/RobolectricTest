package com.example.robolectrictest.view.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.robolectrictest.R
import com.example.robolectrictest.presenter.details.DetailsPresenter
import com.example.robolectrictest.presenter.details.PresenterDetailsContract
import kotlinx.android.synthetic.main.activity_details.*
import java.util.*

class DetailsActivity : AppCompatActivity() {
//
//    private val presenter: PresenterDetailsContract = DetailsPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        supportFragmentManager.beginTransaction()
            .add(
                R.id.detailsFragmentContainer,
                DetailsFragment.newInstance(intent.getIntExtra(TOTAL_COUNT_EXTRA, 0))
            )
            .commitAllowingStateLoss()
    }

    companion object {
        const val TOTAL_COUNT_EXTRA = "TOTAL_COUNT_EXTRA"

        fun getIntent(context: Context, totalCount: Int): Intent {
            return Intent(context, DetailsActivity::class.java).apply {
                putExtra(TOTAL_COUNT_EXTRA, totalCount)
            }
        }
    }
}
