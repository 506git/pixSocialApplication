package com.example.domain.appdata_usecase

import com.example.domain.core.Result
import com.example.domain.model.RoomListInfoDTO
import com.example.domain.repository.AppDataRepository
import kotlinx.coroutines.flow.Flow

class UpdatePushToken(private val repository: AppDataRepository)  {
    suspend operator fun invoke(id : String, token: String): Flow<Result<Unit>> {
        return repository.updatePushToken(id, token)
    }
}