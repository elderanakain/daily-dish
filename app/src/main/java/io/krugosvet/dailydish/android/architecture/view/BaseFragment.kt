package io.krugosvet.dailydish.android.architecture.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import io.krugosvet.bindingcomponent.IBindingContainer
import io.krugosvet.dailydish.android.architecture.aspect.DisposableAspect
import io.krugosvet.dailydish.android.architecture.aspect.IDisposableAspect
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel

abstract class BaseFragment<TBinding : ViewDataBinding, TViewModel : ViewModel<*>> :
  Fragment(),
  IBindingContainer<TBinding, TViewModel>,
  IDisposableAspect by DisposableAspect() {

  abstract override val viewModel: TViewModel

  protected val navController: NavController by lazy { findNavController(this) }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ) =
    bindingComponent.binding.root

  override fun onDestroy() {
    super.onDestroy()

    clearDisposables()
  }
}
