package io.krugosvet.dailydish.android.ui.settings.view

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import io.krugosvet.dailydish.android.R

class SettingsFragment :
    PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}
