package com.example.domain.appdata_usecase

import com.example.domain.core.Result
import com.example.domain.model.RoomListInfoDTO
import com.example.domain.repository.AppDataRepository
import com.example.domain.vo.RoomListInfoVO
import kotlinx.coroutines.flow.Flow

class GetRoomList (private val appRepository: AppDataRepository) {
    suspend operator fun invoke(id : String): Flow<Result<List<RoomListInfoVO>?>> {
        return appRepository.getRoomInfo(id)
    }
}