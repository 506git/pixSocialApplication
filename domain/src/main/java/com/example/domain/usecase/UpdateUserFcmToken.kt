package com.example.domain.usecase

import com.example.domain.preferences.Preferences
import com.example.domain.repository.AppRepository

class UpdateUserFcmToken(private val repository: AppRepository) {
    operator fun invoke(token: String){
        repository.updateUserFcmToken(token)
    }
}