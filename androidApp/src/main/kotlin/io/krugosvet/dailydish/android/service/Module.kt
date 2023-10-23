package io.krugosvet.dailydish.android.service

import androidx.preference.PreferenceManager
import org.koin.dsl.module

val serviceModule = module {
    single { PreferenceService(PreferenceManager.getDefaultSharedPreferences(get()), get()) }
}
