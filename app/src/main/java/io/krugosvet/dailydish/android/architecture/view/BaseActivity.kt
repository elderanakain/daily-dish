package io.krugosvet.dailydish.android.architecture.view

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.findNavController
import io.krugosvet.bindingcomponent.IBindingContainer
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.aspect.DisposableAspect
import io.krugosvet.dailydish.android.architecture.aspect.IDisposableAspect
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

typealias GenericBaseActivity = BaseActivity<*, *>

abstract class BaseActivity<TBinding : ViewDataBinding, TViewModel : ViewModel<*>> :
  AppCompatActivity(),
  IBindingContainer<TBinding, TViewModel>,
  IDisposableAspect by DisposableAspect() {

  val permissionsObservable: Observable<Unit>
    get() = permissionsSubject

  abstract override val viewModel: TViewModel

  protected val navController: NavController by lazy { findNavController(R.id.hostFragment) }

  private val permissionsSubject: BehaviorSubject<Unit> = BehaviorSubject.create()

  override fun onRequestPermissionsResult(
    requestCode: Int, permissions: Array<out String>, grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    permissionsSubject.onNext(Unit)
  }

  override fun onDestroy() {
    super.onDestroy()

    clearDisposables()
  }
}
