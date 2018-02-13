package io.krugosvet.dailydish.android.mainScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.krugosvet.dailydish.android.MealListAdapter
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.utils.RealmFragment
import io.krugosvet.dailydish.android.utils.ViewPagerFragment
import kotlinx.android.synthetic.main.fragment_meal_list.*

private const val PAGE_TITLE = "pageTitle"

class MealListPageFragment : RealmFragment(), ViewPagerFragment {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_meal_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mealList.adapter = MealListAdapter(getRealm())
    }

    override fun getFragmentTitle() = arguments?.getString(PAGE_TITLE) ?: ""

    companion object {
        fun newInstances(vararg pageTitles: String) = mutableListOf<MealListPageFragment>().apply {
            pageTitles.forEach {
                add(MealListPageFragment().apply {
                    arguments = Bundle().apply { putString(PAGE_TITLE, it) }
                })
            }
        }.toTypedArray()
    }
}

