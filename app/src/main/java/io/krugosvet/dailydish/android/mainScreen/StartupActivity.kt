package io.krugosvet.dailydish.android.mainScreen

import android.os.Bundle
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.db.objects.Meal
import io.krugosvet.dailydish.android.utils.RealmActivity
import io.krugosvet.dailydish.android.utils.RealmFragment
import kotlinx.android.synthetic.main.activity_startup.*
import java.util.*

class StartupActivity : RealmActivity(), DialogAddMeal.DialogAddMealListener {

    private lateinit var viewPagerAdapter: ViewPagerAdapter<RealmFragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        setupViewPager()

        floatingButton.setOnClickListener {
            DialogAddMeal().show(fragmentManager, "")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onAddButtonClick(mealTitle: String, mealDescription: String, parseDate: Date) {
        Meal(mealTitle, mealDescription, parseDate).persist(realm)
    }

    private fun setupViewPager() {
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = viewPagerAdapter
        viewPagerAdapter.addFragments(ForTodayFragment.newInstance(getString(R.string.for_today)), MealListPageFragment.newInstance(getString(R.string.earlier)))
        tabs.setupWithViewPager(viewPager, true)
    }
}
