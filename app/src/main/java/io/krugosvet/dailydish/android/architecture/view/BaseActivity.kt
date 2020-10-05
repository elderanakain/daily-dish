package io.krugosvet.dailydish.android.architecture.view

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.findNavController
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.aspect.DisposableAspect
import io.krugosvet.dailydish.android.architecture.aspect.IBindingContainer
import io.krugosvet.dailydish.android.architecture.aspect.IStorageAspect
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

typealias GenericBaseActivity = BaseActivity<*, *>

abstract class BaseActivity<TBinding : ViewDataBinding, TViewModel : ViewModel<*>> :
  AppCompatActivity(),
  IBindingContainer<TBinding, TViewModel>,
  IStorageAspect<Disposable> by DisposableAspect() {

  val permissionsObservable: StateFlow<Int> by lazy { _permissionChangeSignal }

  abstract override val viewModel: TViewModel

  protected val navController: NavController by lazy { findNavController(R.id.hostFragment) }

  private val _permissionChangeSignal = MutableStateFlow(-1)

  override fun onRequestPermissionsResult(
    requestCode: Int, permissions: Array<out String>, grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    _permissionChangeSignal.value = requestCode
  }

  override fun onDestroy() {
    super.onDestroy()

    clear()
  }
}
