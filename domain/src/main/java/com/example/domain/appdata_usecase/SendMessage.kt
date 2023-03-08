package com.example.domain.appdata_usecase

import com.example.domain.core.Result
import com.example.domain.repository.AppDataRepository
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject

class SendMessage (private val repository: AppDataRepository) {

    suspend operator fun invoke(data : JSONObject): Flow<Result<Unit>> {
        return repository.sendMessage(data)
    }
}