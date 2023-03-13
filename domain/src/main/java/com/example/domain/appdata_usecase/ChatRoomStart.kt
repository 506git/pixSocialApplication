package com.example.domain.appdata_usecase

import com.example.domain.core.Result
import com.example.domain.model.FriendsList
import com.example.domain.model.RoomInfoDTO
import com.example.domain.repository.AppDataRepository
import com.example.domain.vo.RoomListInfoVO
import kotlinx.coroutines.flow.Flow

class ChatRoomStart (private val repository: AppDataRepository) {
    suspend operator fun invoke(members: List<String>): Flow<Result<RoomListInfoVO>> {
        return repository.chatRoomStart(members)
    }
}