package com.example.domain.appdata_usecase

import com.example.domain.core.Result
import com.example.domain.model.User
import com.example.domain.repository.AppDataRepository
import com.example.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow

class AddFriend(private val repository: AppDataRepository) {
    suspend operator fun invoke(friendsEmail: String): Flow<Result<Unit>> {
        return repository.addFriends(friendsEmail)
    }
}