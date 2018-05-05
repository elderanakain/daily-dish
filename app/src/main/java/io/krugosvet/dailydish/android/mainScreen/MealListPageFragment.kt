package io.krugosvet.dailydish.android.mainScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.db.objects.Meal
import io.krugosvet.dailydish.android.utils.intent.ImageProviderActivity
import io.krugosvet.dailydish.android.utils.RealmFragment
import io.krugosvet.dailydish.android.utils.ViewPagerFragment
import io.krugosvet.dailydish.android.utils.getMeals
import io.realm.OrderedRealmCollection
import kotlinx.android.synthetic.main.fragment_meal_list.*

const val NO_LIMIT = -1
const val PAGE_TITLE = "pageTitle"

open class MealListPageFragment : RealmFragment(), ViewPagerFragment {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_meal_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealList.adapter = MealListAdapter(getRealm(), getAdapterItems(), getAdapterItemLimit(), activity as ImageProviderActivity)
    }

    override fun getFragmentTitle() = arguments?.getString(PAGE_TITLE) ?: ""

    protected open fun getAdapterItems(): OrderedRealmCollection<Meal> = getRealm().getMeals()

    protected open fun getAdapterItemLimit() = NO_LIMIT

    companion object {
        fun newInstance(pageTitle: String) = MealListPageFragment().apply {
            arguments = Bundle().apply { putString(PAGE_TITLE, pageTitle) }
        }
    }
}

