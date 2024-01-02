package com.example.imentor.main

import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.example.imentor.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val syncPreference = findPreference<SwitchPreferenceCompat>("notifications")
        syncPreference?.setOnPreferenceChangeListener { preference, newValue ->
            val allowNotifications = newValue as Boolean
            Log.i("PERS", allowNotifications.toString())
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val editor = sharedPreferences.edit()
            editor.putBoolean("notifications", allowNotifications)
            editor.apply()
            true
        }
    }
}