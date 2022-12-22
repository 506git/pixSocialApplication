package com.example.domain.usecase

import com.example.domain.core.Result
import com.example.domain.model.RoomChat
import com.example.domain.model.RoomInfo
import com.example.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class GetRoomChat (private val appRepository: AppRepository) {
    operator fun invoke(roomId: String):  Flow<Result<List<RoomChat>>> {
        return appRepository.getRoomChat(roomId)
    }
}