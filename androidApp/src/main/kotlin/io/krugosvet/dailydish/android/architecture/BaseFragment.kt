package io.krugosvet.dailydish.android.architecture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel.State
import io.krugosvet.dailydish.android.errorHandler
import io.krugosvet.dailydish.android.ui.container.ContainerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

abstract class BaseFragment<TBinding : ViewDataBinding, TViewModel : ViewModel<*>>(
    @LayoutRes val layout: Int,
) :
    Fragment(),
    BindingContainer<TBinding, TViewModel> {

    abstract override val viewModel: TViewModel

    override val bindingComponent by lazy { BindingImpl(layout, this) }

    protected val navController: NavController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) =
        binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach { state ->
                val container = activity as ContainerActivity

                container.binding.progressBar.isVisible = state is State.Loading
            }
            .launchInCatching()
    }

    protected inline fun launch(crossinline block: suspend CoroutineScope.() -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            block()
        }
    }

    protected fun <T> Flow<T>.launchInCatching(): Job =
        launchIn(lifecycleScope + errorHandler)
}
