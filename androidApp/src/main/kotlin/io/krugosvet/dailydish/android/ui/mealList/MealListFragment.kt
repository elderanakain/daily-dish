package io.krugosvet.dailydish.android.ui.mealList

import android.os.Bundle
import android.view.View
import androidx.lifecycle.flowWithLifecycle
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.BaseFragment
import io.krugosvet.dailydish.android.databinding.FragmentMealListBinding
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MealListFragment :
    BaseFragment<FragmentMealListBinding, MealListViewModel>(R.layout.fragment_meal_list) {

    override val viewModel: MealListViewModel by viewModel()

    private val mealListDecorator: MealListDecorator by inject()

    private val adapter: MealListAdapter by lazy { MealListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mealList.apply {
            adapter = this@MealListFragment.adapter

            if (itemDecorationCount == 0) {
                addItemDecoration(mealListDecorator)
            }
        }

        viewModel.mealList
            .flowWithLifecycle(lifecycle)
            .onEach(adapter::submitList)
            .launchInCatching()
    }
}
