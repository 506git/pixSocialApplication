package com.example.data.repository

import android.content.Context
import com.example.data.dto.FriendsAddDTO

import com.example.data.model.UserDTO
import com.example.data.repository.dataSource.RemoteDataSource
import com.example.data.service.PushService
import com.example.domain.core.Result
import com.example.domain.model.FriendsList
import com.example.domain.model.User
import com.example.domain.model.UserInfoDTO
import com.example.domain.repository.AppDataRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppDataRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val TestRemoteSource: RemoteDataSource,
    private val context: Context,
    private val pushService: PushService,
) : AppDataRepository {
    override suspend fun googleAutoLogIn(): Flow<Result<UserInfoDTO>> =  callbackFlow {
        send(Result.Loading())
        val user = auth.currentUser

        user?.run {
            getIdToken(true).addOnSuccessListener { result ->
                val token = result.token.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    runCatching {
                        return@runCatching TestRemoteSource.googleLogin(token)
                    }.onSuccess {
                        val json = Gson().toJson(it)
                        val userInfo = Gson().fromJson(json, UserInfoDTO::class.java)
                        trySend(Result.Success(userInfo))
                    }.onFailure { e ->
                        trySend(Result.Error(Exception(e)))
                    }
                }
            }.addOnFailureListener {
                trySend(Result.Error(it))
            }
        } ?: trySend(Result.Error(NullPointerException()))

        awaitClose {
            channel.close()
        }
    }

    override suspend fun signInWithCredential(idToken : String): Flow<Result<UserInfoDTO>> =  callbackFlow {
        send(Result.Loading())

        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential).addOnSuccessListener {
            if (it.user != null) {
                it.user!!.getIdToken(true).addOnSuccessListener { result ->
                    val token = result.token.toString()
                    CoroutineScope(Dispatchers.IO).launch {
                        runCatching {
                            return@runCatching TestRemoteSource.googleLogin(token)
                        }.onSuccess {
                            val json = Gson().toJson(it)
                            val userInfo = Gson().fromJson(json, UserInfoDTO::class.java)
                            trySend(Result.Success(userInfo))
                        }.onFailure { e ->
                            trySend(Result.Error(Exception(e)))
                        }
                    }
                }
            }
        }.addOnFailureListener {
            trySend(Result.Error(it))
        }

        awaitClose {
            channel.close()
        }
    }

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

    override suspend fun getFriendsList(id: String): Flow<Result<FriendsList>> = callbackFlow{
        send(Result.Loading())

        runCatching {
            return@runCatching TestRemoteSource.getFriendsList(UserDTO(userId = id))
        }.onSuccess {
            val json = Gson().toJson(it)
            val friendsList = Gson().fromJson(json, FriendsList::class.java)
            trySend(Result.Success(friendsList))
        }.onFailure { e ->
            trySend(Result.Error(Exception(e)))
        }

        awaitClose {
            channel.close()
        }
    }

}