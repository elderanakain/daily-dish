package io.krugosvet.dailydish.android.mainScreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.utils.ViewPagerFragment
import io.krugosvet.dailydish.android.utils.baseUi.BaseFragment
import io.krugosvet.dailydish.android.utils.getAscByDateMeals
import io.krugosvet.dailydish.android.utils.intent.ImageProviderActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_meal_list.*
import javax.inject.Inject

const val PAGE_TITLE = "pageTitle"

open class MealListPageFragment : BaseFragment(), ViewPagerFragment {

    @Inject
    protected lateinit var accountStateChangeReceiver: Observable<Intent>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_meal_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mealList.adapter = MealListAdapter(realm, activity as ImageProviderActivity,
                { realm.getAscByDateMeals(authTokenManager.userId()) }, accountStateChangeReceiver)
    }

    override fun getFragmentTitle() = arguments?.getString(PAGE_TITLE) ?: ""

    companion object {
        fun newInstance(pageTitle: String) = MealListPageFragment().apply {
            arguments = Bundle().apply { putString(PAGE_TITLE, pageTitle) }
        }
    }
}

