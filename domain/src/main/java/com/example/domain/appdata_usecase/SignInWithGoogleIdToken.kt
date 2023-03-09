package com.example.domain.appdata_usecase

import com.example.domain.core.Result
import com.example.domain.model.UserInfoVO
import com.example.domain.repository.AppDataRepository
import kotlinx.coroutines.flow.Flow

class SignInWithGoogleIdToken(private val repository: AppDataRepository) {
    suspend operator fun invoke(idToken: String): Flow<Result<UserInfoVO>> {
        return repository.signInWithCredential(idToken)
    }
}