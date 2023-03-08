package com.example.domain.appdata_usecase

import com.example.domain.core.Result
import com.example.domain.model.RoomChat
import com.example.domain.repository.AppDataRepository
import com.example.domain.repository.AppRepository
import com.example.domain.vo.ChatListVO
import kotlinx.coroutines.flow.Flow

class GetChatList(private val appRepository: AppDataRepository) {

    suspend operator fun invoke(roomId: String): Flow<Result<List<ChatListVO>?>> {
        return appRepository.getChatList(roomId)
    }
}