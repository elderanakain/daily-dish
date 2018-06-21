package io.krugosvet.dailydish.android.mainScreen

import android.graphics.Bitmap
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View
import io.krugosvet.dailydish.android.DailyDishApplication
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.db.objects.Meal
import io.krugosvet.dailydish.android.utils.baseUi.BaseFragment
import io.krugosvet.dailydish.android.utils.bytesFromBitmap
import io.krugosvet.dailydish.android.utils.intent.ImageProviderActivity
import io.reactivex.CompletableObserver
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable
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
            DialogAddMeal().addCameraImagePipe(this).show(fragmentManager, "")
        }

        mealServicePipe.getMeals().subscribe(object : MaybeObserver<List<Meal>> {
            override fun onSubscribe(d: Disposable) {
                progressBar.visibility = View.VISIBLE
            }

            override fun onSuccess(meals: List<Meal>) {
                realm.executeTransaction { it.insertOrUpdate(meals) }
                onFinish(R.string.network_success_message)
            }

            override fun onComplete() {
                onFinish(R.string.network_success_message)
            }

            override fun onError(e: Throwable) {
                onFinish(R.string.network_error_message)
            }

            private fun onFinish(@StringRes stringId: Int) {
                progressBar.visibility = View.INVISIBLE
                Snackbar.make(parentCoordinatorLayout, getString(stringId),
                        Snackbar.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onAddButtonClick(mealTitle: String, mealDescription: String, parseDate: Date, mainImage: Bitmap?) {
        bytesFromBitmap(mainImage).subscribe { image ->
            val meal = Meal(mealTitle, mealDescription, parseDate, image, authTokenManager.userId())

            mealServicePipe.sendMeal(meal).subscribe(object : CompletableObserver {
                override fun onComplete() {
                    onFinish(R.string.network_post_meal_success)
                    meal.persist(realm)
                }

                override fun onSubscribe(d: Disposable) {
                    progressBar.visibility = View.VISIBLE
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    onFinish(R.string.network_post_meal_error)
                }

                private fun onFinish(@StringRes stringId: Int) {
                    progressBar.visibility = View.INVISIBLE
                    Snackbar.make(parentCoordinatorLayout, getString(stringId),
                            Snackbar.LENGTH_LONG).show()
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
