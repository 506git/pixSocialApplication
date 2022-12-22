package com.example.domain.usecase

import com.example.domain.core.Result
import com.example.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class SendChat (private val appRepository: AppRepository) {
    suspend operator fun invoke(message: String, roomId: String): Flow<Result<Unit>> {
        return appRepository.sendChat(message, roomId)
    }
}