package com.example.domain.usecase

import com.example.domain.core.Result
import com.example.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class RemoveChat (private val appRepository: AppRepository) {
    suspend operator fun invoke(messageId: String, roomId : String): Flow<Result<Unit>> {
        return appRepository.removeChat(messageId, roomId)
    }
}