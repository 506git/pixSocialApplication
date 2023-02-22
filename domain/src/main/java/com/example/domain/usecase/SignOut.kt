package com.example.domain.usecase

import com.example.domain.repository.AppRepository

class SignOut(
    private val repository: AppRepository
) {
    operator fun invoke() {
        repository.signOut()
    }
}