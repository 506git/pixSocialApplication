package com.example.domain.appdata_usecase

import com.example.domain.core.Result
import com.example.domain.repository.AppDataRepository
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject

class LeaveRoom (private val repository: AppDataRepository) {

    suspend operator fun invoke(data : JSONObject): Flow<Result<Unit>> {
        return repository.leaveRoom(data)
    }
}