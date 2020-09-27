package io.krugosvet.dailydish.android.service

import android.content.SharedPreferences
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.core.service.ResourceService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@FlowPreview
@ExperimentalCoroutinesApi
class PreferenceService(
  private val sharedPreferences: SharedPreferences,
  resourceService: ResourceService
) {

  val isReminderEnabled: Flow<Boolean> by lazy {
    flowOf(
      flow { emit(sharedPreferences.getBoolean(reminderKey, false)) },
      preferenceKeyObservable
        .filter { key -> key == reminderKey }
        .map { key -> sharedPreferences.getBoolean(key, false) }
    ).flattenMerge()
  }

  private val preferenceKeyObservable: Flow<String> =
    callbackFlow {
      sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
        sendBlocking(key)
      }

      awaitClose()
    }

  private val reminderKey: String = resourceService.getString(R.string.preference_reminder_key)
}
