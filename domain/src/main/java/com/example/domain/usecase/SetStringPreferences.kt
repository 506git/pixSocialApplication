package com.example.domain.usecase

import com.example.domain.preferences.Preferences

class SetStringPreferences(private val preferences: Preferences) {
    operator fun invoke(key : String, value: String){
        preferences.setStringPreferences(key, value)
    }
}
