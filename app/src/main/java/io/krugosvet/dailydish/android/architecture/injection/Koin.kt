package io.krugosvet.dailydish.android.architecture.injection

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.scope.lifecycleScope
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import kotlin.properties.ReadOnlyProperty

val coreModule = module {

  single {
    androidContext().resources
  }
}

inline fun <reified T : Any> AppCompatActivity.activityInject() =
  lifecycleScope.inject<T> {
    parametersOf(this)
  }

inline fun <reified T : Any> Fragment.activityInject() =
  ReadOnlyProperty<Any, T> { _, _ ->
    with(requireActivity()) {
      lifecycleScope.get { parametersOf(this) }
    }
  }
