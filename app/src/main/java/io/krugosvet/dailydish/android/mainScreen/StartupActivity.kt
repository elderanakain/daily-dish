package io.krugosvet.dailydish.android.mainScreen

import android.graphics.Bitmap
import android.os.Bundle
import io.krugosvet.dailydish.android.DailyDishApplication
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.db.objects.Meal
import io.krugosvet.dailydish.android.network.BaseNetworkObserver
import io.krugosvet.dailydish.android.network.json.MealId
import io.krugosvet.dailydish.android.utils.baseUi.BaseFragment
import io.krugosvet.dailydish.android.utils.baseUi.showLongSnackbar
import io.krugosvet.dailydish.android.utils.bytesFromBitmap
import io.krugosvet.dailydish.android.utils.intent.ImageProviderActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class StartupActivity : ImageProviderActivity(), DialogAddMeal.DialogAddMealListener {

    private lateinit var viewPagerAdapter: ViewPagerAdapter<BaseFragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DailyDishApplication.appComponent.inject(this)

        setupViewPager()

        floatingButton.setOnClickListener {
            if (isInternetConnection()) {
                DialogAddMeal().addCameraImagePipe(this).show(fragmentManager, "")
            } else showLongSnackbar(this, R.string.network_no_internet_connection)
        }

        mealServicePipe.getMeals().subscribe(object : BaseNetworkObserver<List<Meal>>(this) {
            override val onErrorMessage: Int = R.string.network_error_message

            override fun onSuccess(meals: List<Meal>) {
                realm.executeTransaction { it.insertOrUpdate(meals) }
                onFinish(R.string.network_success_message)
            }

            override fun onComplete() {
                onFinish(R.string.network_success_message)
            }
        })
    }

    override fun onAddButtonClick(mealTitle: String, mealDescription: String, parseDate: Date, mainImage: Bitmap?) {
        bytesFromBitmap(mainImage).subscribe { image ->
            val meal = Meal(mealTitle, mealDescription, parseDate, image, authTokenManager.userId())
            mealServicePipe.sendMeal(meal).subscribe(object : BaseNetworkObserver<MealId>(this) {

                override val onErrorMessage: Int = R.string.network_post_meal_error

                override fun onSuccess(mealId: MealId) {
                    onFinish(R.string.network_post_meal_success)
                    meal.persist(realm, mealId.id)
                }
            })
        }
    }

    private fun setupViewPager() {
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = viewPagerAdapter
        viewPagerAdapter.addFragments(ForTodayFragment.newInstance(getString(R.string.for_today)), MealListPageFragment.newInstance(getString(R.string.earlier)))
        tabs.setupWithViewPager(viewPager, true)
    }
}
