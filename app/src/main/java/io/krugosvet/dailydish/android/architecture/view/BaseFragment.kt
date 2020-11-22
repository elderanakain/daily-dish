package io.krugosvet.dailydish.android.architecture.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import io.krugosvet.dailydish.android.architecture.aspect.DisposableAspect
import io.krugosvet.dailydish.android.architecture.aspect.IBindingContainer
import io.krugosvet.dailydish.android.architecture.aspect.IStorageAspect
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel.State
import io.krugosvet.dailydish.android.ui.container.view.ContainerActivity
import io.reactivex.disposables.Disposable

abstract class BaseFragment<TBinding : ViewDataBinding, TViewModel : ViewModel<*>> :
  Fragment(),
  IBindingContainer<TBinding, TViewModel>,
  IStorageAspect<Disposable> by DisposableAspect() {

  abstract override val viewModel: TViewModel

  protected val navController: NavController by lazy { findNavController(this) }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ) =
    binding.root

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.state.observe(viewLifecycleOwner) { state ->
      val container = activity as ContainerActivity

      container.binding.progressBar.isVisible = state is State.Loading
    }
  }

  override fun onDestroy() {
    super.onDestroy()

    clear()
  }

  protected inline fun <T> LiveData<T>.observe(crossinline block: (value: T) -> Unit) {
    observe(viewLifecycleOwner) {
      block(it)
    }
  }
}
