package io.krugosvet.dailydish.android.architecture.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import io.krugosvet.dailydish.android.DailyDishApplication
import io.krugosvet.dailydish.android.dagger.AppComponent
import io.krugosvet.dailydish.android.network.MealServicePipe
import io.realm.Realm
import javax.inject.Inject

abstract class BaseFragment<VB : ViewDataBinding> :
  DialogFragment() {

  lateinit var binding: VB
    private set

  @Inject
  protected lateinit var realm: Realm

  @Inject
  protected lateinit var mealServicePipe: MealServicePipe

  @get:LayoutRes
  protected abstract val layoutId: Int

  protected abstract fun inject(appComponent: AppComponent)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    inject(DailyDishApplication.appComponent)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {

    binding = DataBindingUtil.inflate(inflater, layoutId, container, false)

    return binding.root
  }

  override fun onDestroy() {
    super.onDestroy()

    realm.close()
  }
}
