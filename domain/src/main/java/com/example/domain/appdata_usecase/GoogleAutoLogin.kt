package com.example.domain.appdata_usecase

import com.example.domain.core.Result
import com.example.domain.model.UserInfoDTO
import com.example.domain.repository.AppDataRepository
import kotlinx.coroutines.flow.Flow

class GoogleAutoLogin(private val repository: AppDataRepository) {

    suspend operator fun invoke(): Flow<Result<UserInfoDTO>> {
        return repository.googleAutoLogIn()
    }
}