package com.example.domain.appdata_usecase

import com.example.domain.core.Result
import com.example.domain.model.FriendsList
import com.example.domain.repository.AppDataRepository
import com.example.domain.vo.FriendsInfoVO
import kotlinx.coroutines.flow.Flow

class GetFriendsList(private val repository: AppDataRepository) {
    suspend operator fun invoke(id: String): Flow<Result<List<FriendsInfoVO>?>> {
        return repository.getFriendsList(id)
    }
}