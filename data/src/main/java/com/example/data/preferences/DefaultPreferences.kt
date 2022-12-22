package com.example.data.preferences

import android.content.SharedPreferences
import com.example.domain.preferences.Preferences

class DefaultPreferences( private val sharedPref: SharedPreferences): Preferences {
    override fun saveIntroViewStatus(show: Boolean) {
        sharedPref.edit()
            .putBoolean(Preferences.INTRO_NOT_USE, show)
            .apply()
    }

    override fun loadIntroViewStatus(): Boolean {
        return sharedPref.getBoolean(
            Preferences.INTRO_NOT_USE,
            false
        )
    }


    override fun setStringPreferences(key: String, value: String) {
        sharedPref.edit()
            .putString(key, value)
            .apply()
    }

    override fun getStringPreferences(key: String): String {
        return sharedPref.getString(key, "").toString()
    }
}