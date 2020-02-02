package io.krugosvet.dailydish.android.utils.baseUi

import android.os.*
import androidx.fragment.app.*
import io.krugosvet.dailydish.android.network.*
import io.realm.*
import javax.inject.*

abstract class BaseFragment : Fragment() {

  @Inject
  protected lateinit var realm: Realm
  @Inject
  protected lateinit var mealServicePipe: MealServicePipe

  protected abstract fun initInjection()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initInjection()
  }

  override fun onDestroy() {
    super.onDestroy()
    realm.close()
  }
}
