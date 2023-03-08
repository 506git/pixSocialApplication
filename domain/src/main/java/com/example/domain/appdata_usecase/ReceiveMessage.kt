package com.example.domain.appdata_usecase

import com.example.domain.core.Result
import com.example.domain.repository.AppDataRepository
import com.example.domain.vo.MessageVO
import kotlinx.coroutines.flow.Flow

class ReceiveMessage (private val appRepository: AppDataRepository) {
    suspend operator fun invoke(): Flow<Result<MessageVO>> {
        return appRepository.receiveMessage()
    }
}
