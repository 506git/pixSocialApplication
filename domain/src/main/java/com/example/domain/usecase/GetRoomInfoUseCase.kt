package com.example.domain.usecase

import com.example.domain.model.RoomInfo
import com.example.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import com.example.domain.core.Result

class GetRoomInfoUseCase (private val appRepository: AppRepository) {
    operator fun invoke():  Flow<Result<List<RoomInfo>>> {
        return appRepository.getRoomInfo()
    }
}