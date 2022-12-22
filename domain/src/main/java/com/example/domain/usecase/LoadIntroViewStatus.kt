package com.example.domain.usecase

import com.example.domain.preferences.Preferences

class LoadIntroViewStatus(private val preferences: Preferences) {
    operator fun invoke() : Boolean{
        return preferences.loadIntroViewStatus()
    }
}