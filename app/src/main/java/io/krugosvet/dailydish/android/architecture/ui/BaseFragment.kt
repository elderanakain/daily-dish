package io.krugosvet.dailydish.android.architecture.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import io.krugosvet.bindingcomponent.IBindingContainer
import io.krugosvet.dailydish.android.DailyDishApplication
import io.krugosvet.dailydish.android.dagger.AppComponent
import io.krugosvet.dailydish.android.network.MealServicePipe
import io.realm.Realm
import javax.inject.Inject

abstract class BaseFragment<TBinding: ViewDataBinding, TVisual> :
  DialogFragment(),
  IBindingContainer<TBinding, TVisual> {

  @Inject
  protected lateinit var realm: Realm

  @Inject
  protected lateinit var mealServicePipe: MealServicePipe

  protected abstract fun inject(appComponent: AppComponent)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    inject(DailyDishApplication.appComponent)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View =
    bindingComponent.binding.root

  override fun onDestroy() {
    super.onDestroy()

    realm.close()
  }
}
