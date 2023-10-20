package com.example.a07_data

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        // Shows what you selected for the color
        val colorPreference : ListPreference? = findPreference("backcolor")
        colorPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
    }
}