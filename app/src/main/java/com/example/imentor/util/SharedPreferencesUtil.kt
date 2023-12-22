package com.example.imentor.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesUtil(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("NotificationPrefs", Context.MODE_PRIVATE)

    fun isNotificationsEnabled(): Boolean {
        return sharedPreferences.getBoolean("notificationsEnabled", true)
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("notificationsEnabled", enabled).apply()
    }

    fun getNotificationFrequency(): Int {
        return sharedPreferences.getInt("notificationFrequency", 1)
    }

    fun setNotificationFrequency(frequency: Int) {
        sharedPreferences.edit().putInt("notificationFrequency", frequency).apply()
    }
}
