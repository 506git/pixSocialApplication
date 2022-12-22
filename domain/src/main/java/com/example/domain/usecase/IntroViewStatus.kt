package com.example.domain.usecase

import com.example.domain.preferences.Preferences

class IntroViewStatus(private val preferences: Preferences) {
    operator fun invoke(show: Boolean){
        preferences.saveIntroViewStatus(show)
    }
}