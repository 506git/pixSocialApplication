package com.example.domain.usecase

import com.example.domain.repository.AppRepository
import com.example.domain.core.Result
import kotlinx.coroutines.flow.Flow

class SignInWithGoogleIdToken(private val repository: AppRepository) {
    suspend operator fun invoke(idToken: String): Flow<Result<Unit>> {
        return repository.signInWithCredential(idToken)
    }
}