package io.krugosvet.dailydish.android.architecture.view

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.aspect.IBindingContainer
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.errorHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.plus

abstract class BaseActivity<TBinding : ViewDataBinding, TViewModel : ViewModel<*>> :
    AppCompatActivity(),
    IBindingContainer<TBinding, TViewModel> {

    abstract override val viewModel: TViewModel

    protected val navController: NavController by lazy { findNavController(R.id.hostFragment) }

    protected fun <T> Flow<T>.launchInCatching(): Job =
        launchIn(lifecycleScope + errorHandler)
}
