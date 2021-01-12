package io.krugosvet.dailydish.android.architecture.injection

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.koin.androidx.scope.activityScope
import org.koin.core.parameter.parametersOf
import kotlin.properties.ReadOnlyProperty

inline fun <reified T : Any> AppCompatActivity.activityInject() =
  activityScope().inject<T> {
    parametersOf(this)
  }

inline fun <reified T : Any> Fragment.activityInject() =
  ReadOnlyProperty<Any, T> { _, _ ->
    with(requireActivity()) {
      activityScope().get { parametersOf(this) }
    }
  }
