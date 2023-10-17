package io.krugosvet.dailydish.android.ui.mealList

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.flowWithLifecycle
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.aspect.BindingComponent
import io.krugosvet.dailydish.android.architecture.view.BaseFragment
import io.krugosvet.dailydish.android.databinding.FragmentMealListBinding
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class MealListFragment :
    BaseFragment<FragmentMealListBinding, MealListViewModel>() {

    override val parentContext: Context
        get() = requireContext()

    override val viewModel: MealListViewModel by stateViewModel()
    override val bindingComponent =
        BindingComponent(R.layout.fragment_meal_list, this)

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

        viewModel.navigationEvent
            .flowWithLifecycle(lifecycle)
            .onEach {}
            .launchInCatching()
    }
}

