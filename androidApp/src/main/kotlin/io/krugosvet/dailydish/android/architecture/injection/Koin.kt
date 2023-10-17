package io.krugosvet.dailydish.android.architecture.injection

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.scope.activityRetainedScope
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val coreModule = module {

    single {
        androidContext().resources
    }
}

inline fun <reified T : Any> AppCompatActivity.activityInject() =
    activityRetainedScope().value.inject<T> {
        parametersOf(this)
    }

inline fun <reified T : Any> Fragment.activityInject() =
    ReadOnlyProperty<Any, T> { _, _ ->
        with(requireActivity()) {
            this.activityRetainedScope().value.get()
        }
    }
