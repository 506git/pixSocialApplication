package com.example.data.repository

import android.content.Context
import com.example.data.db.UserDao
import com.example.data.dto.FriendsAddDTO
import com.example.data.mapper.UserMapper
import com.example.data.repository.dataSource.TestRemoteDataSource
import com.example.data.service.PushService
import com.example.domain.core.Result
import com.example.domain.model.User
import com.example.domain.repository.AppDataRepository
import com.example.domain.repository.DatabaseRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AppDataRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val TestRemoteSource: TestRemoteDataSource,
    private val context: Context,
    private val pushService: PushService,
) : AppDataRepository {

    override suspend fun addFriends(friendsEmail: String): Flow<Result<Unit>> = callbackFlow {
        send(Result.Loading())
        val user = auth.currentUser

        if (user != null) {
            runCatching {
                TestRemoteSource.addFriendsAdd(FriendsAddDTO(requestUserEmail = user.email, receiveUserEmail = friendsEmail))
            }.onSuccess {
                trySend(Result.Success(Unit))
            }.onFailure { e ->
                trySend(Result.Error(Exception(e)))
            }
        }

        awaitClose {
            channel.close()
        }
    }

    override suspend fun getUserInfo(id: String): Flow<Result<User>> = callbackFlow{
        send(Result.Loading())

        runCatching {
            return@runCatching TestRemoteSource.getUserInfo(id) as User
        }.onSuccess { it ->
            trySend(Result.Success(it))
        }.onFailure { e ->
            trySend(Result.Error(Exception(e)))
        }

        awaitClose {
            channel.close()
        }
    }

}