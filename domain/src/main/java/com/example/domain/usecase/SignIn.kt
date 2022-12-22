package com.example.domain.usecase

import com.example.domain.repository.AppRepository
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

class SignIn(private val repository: AppRepository) {
    suspend operator fun invoke(email: String, password: String): Flow<Result<Unit>> {
        return repository.signIn(email, password)
    }
}