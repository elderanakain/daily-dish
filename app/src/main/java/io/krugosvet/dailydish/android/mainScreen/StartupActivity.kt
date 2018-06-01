package io.krugosvet.dailydish.android.mainScreen

import android.os.Bundle
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.db.objects.Meal
import io.krugosvet.dailydish.android.utils.baseUi.BaseFragment
import io.krugosvet.dailydish.android.utils.intent.ImageProviderActivity
import io.krugosvet.dailydish.android.utils.readBytesFromFile
import kotlinx.android.synthetic.main.activity_startup.*
import java.io.File
import java.util.*

class StartupActivity : ImageProviderActivity(), DialogAddMeal.DialogAddMealListener {

    private lateinit var viewPagerAdapter: ViewPagerAdapter<BaseFragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        setupViewPager()

        floatingButton.setOnClickListener {
            DialogAddMeal().addCameraImagePipe(this).show(fragmentManager, "")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onAddButtonClick(mealTitle: String, mealDescription: String, parseDate: Date, mainImage: File?) {
        Meal(mealTitle, mealDescription, parseDate, readBytesFromFile(mainImage), authTokenManager.userId()).persist(realm)
    }

    private fun setupViewPager() {
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = viewPagerAdapter
        viewPagerAdapter.addFragments(ForTodayFragment.newInstance(getString(R.string.for_today)), MealListPageFragment.newInstance(getString(R.string.earlier)))
        tabs.setupWithViewPager(viewPager, true)
    }
}
