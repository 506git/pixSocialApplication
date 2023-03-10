package com.example.domain.appdata_usecase

import com.example.domain.core.Result
import com.example.domain.repository.AppDataRepository
import kotlinx.coroutines.flow.Flow

class UploadImage(private val repository: AppDataRepository)  {
    operator fun invoke(path : String, userId: String): Flow<Result<String>> {
        return repository.uploadImage(path, userId)
    }
}