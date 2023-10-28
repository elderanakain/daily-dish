package io.krugosvet.dailydish.android.ui.mealList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.BaseFragment
import io.krugosvet.dailydish.android.databinding.FragmentMealListBinding
import javax.inject.Inject
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MealListFragment :
    BaseFragment<FragmentMealListBinding, MealListViewModel>(R.layout.fragment_meal_list) {

    override val viewModel: MealListViewModel by viewModels()

    @Inject
    lateinit var mealListDecorator: MealListDecorator

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
