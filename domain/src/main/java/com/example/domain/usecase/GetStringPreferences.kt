package com.example.domain.usecase

import com.example.domain.preferences.Preferences

class GetStringPreferences(private val preferences: Preferences) {
    operator fun invoke(key:String) : String{
        return preferences.getStringPreferences(key)
    }
}