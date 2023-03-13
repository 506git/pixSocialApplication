package com.example.data.repository

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.example.domain.vo.ChatListVO
import com.example.data.dto.CreateRoomDTO
import com.example.data.dto.FriendsAddDTO
import com.example.data.model.UserDTO
import com.example.data.repository.dataSource.RemoteDataSource
import com.example.data.service.PushService
import com.example.domain.core.Result
import com.example.domain.model.*
import com.example.domain.preferences.Preferences
import com.example.domain.repository.AppDataRepository
import com.example.domain.socket.AppSocket
import com.example.domain.vo.MessageVO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

class AppDataRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val TestRemoteSource: RemoteDataSource,
    private val firebaseStorage: FirebaseStorage,
    private val context: Context,
    private val sharedPreferences: Preferences,
    private val socket: AppSocket
) : AppDataRepository {
    override suspend fun googleAutoLogIn(): Flow<Result<UserInfoVO>> =  callbackFlow {
        send(Result.Loading())
        val user = auth.currentUser

        user?.run {
            getIdToken(true).addOnSuccessListener { result ->
                val token = result.token.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    runCatching {
                        return@runCatching TestRemoteSource.googleLogin(token).result.content!!
                    }.mapCatching {
                        UserInfoVO(
                            _id = it._id,
                            user_id = it.user_id,
                            name = it.name,
                            email = it.email,
                            picture = it.picture,
                            createdAt = it.createdAt,
                            updatedAt = it.updatedAt,
                            comment = it.comment
                        )
                    }.onSuccess {
                        trySend(Result.Success(it))
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

    override suspend fun signInWithCredential(idToken : String): Flow<Result<UserInfoVO>> =  callbackFlow {
        send(Result.Loading())

        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential).addOnSuccessListener {
            if (it.user != null) {
                it.user!!.getIdToken(true).addOnSuccessListener { result ->
                    val token = result.token.toString()
                    CoroutineScope(Dispatchers.IO).launch {
                        runCatching {
                            return@runCatching TestRemoteSource.googleLogin(token).result.content!!
                        }.mapCatching { it ->
                            UserInfoVO(
                                _id = it._id,
                                user_id = it.user_id,
                                name = it.name,
                                email = it.email,
                                picture = it.picture,
                                createdAt = it.createdAt,
                                updatedAt = it.updatedAt,
                                comment = it.comment
                            )
                        }.onSuccess {
//                            val json = Gson().toJson(it)
//                            val userInfo = Gson().fromJson(json, UserInfoVO::class.java)
//                            trySend(Result.Success(userInfo))
                            trySend(Result.Success(it))
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

    override suspend fun updatePushToken(userId : String, token: String): Flow<Result<Unit>> = callbackFlow{
        send(Result.Loading())

        runCatching {
            Log.d("TESTPUSH", "START")
            TestRemoteSource.updatePushToken(userId, token)
        }.onSuccess { it ->
            Log.d("TESTPUSH", "Succ")
            trySend(Result.Success(Unit))
        }.onFailure { e ->
            Log.d("TESTPUSH", "$e")
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

    override suspend fun chatRoomStart(members: List<String>): Flow<Result<RoomInfoDTO>> = callbackFlow{
        send(Result.Loading())

        runCatching {
            return@runCatching TestRemoteSource.createRoom(CreateRoomDTO(
                roomName = "",
                memberIds = members
            ))
        }.onSuccess {
            val json = Gson().toJson(it)
            val roomInfo = Gson().fromJson(json, RoomInfoDTO::class.java)
            trySend(Result.Success(roomInfo))
        }.onFailure { e ->
            trySend(Result.Error(Exception(e)))
        }

        awaitClose {
            channel.close()
        }
    }

    override suspend fun getRoomInfo(id: String): Flow<Result<RoomListInfoDTO>> = callbackFlow{
        send(Result.Loading())

        runCatching {
            return@runCatching TestRemoteSource.getRoomList(UserDTO(userId = id))
        }.onSuccess {
            val json = Gson().toJson(it)
            val roomListInfo = Gson().fromJson(json, RoomListInfoDTO::class.java)
            trySend(Result.Success(roomListInfo))
        }.onFailure { e ->
            trySend(Result.Error(Exception(e)))
        }

        awaitClose {
            channel.close()
        }
    }

    override suspend fun getChatList(roomId: String): Flow<Result<List<ChatListVO>?>> = callbackFlow {
        send(Result.Loading())

        runCatching {
            return@runCatching TestRemoteSource.getChatList(roomId)
        }.mapCatching {
            it.result.content?.filter { it ->
                !it.user_id.isNullOrEmpty()
            }?.map { it ->
                ChatListVO(
                    chat_id = it._id,
                    user_id = it.user_id,
                    message_body = it.message_body,
                    message_type = it.message_type,
                    createdAt = it.createdAt
                )
            }
        }.onSuccess { it ->
            trySend(Result.Success(it))
        }.onFailure { e ->
            trySend(Result.Error(Exception(e)))
        }

        awaitClose {
            channel.close()
        }
    }

    override suspend fun joinRoom(data: JSONObject): Flow<Result<Unit>> = callbackFlow{
        send(Result.Loading())

        try {
            socket.connect()

            socket.emit("joinRoom", data)

            trySend(Result.Success(Unit))
        } catch (e: Exception){
            trySend(Result.Error(e))
        }

        awaitClose {
            channel.close()
        }
    }

    override suspend fun leaveRoom(data: JSONObject): Flow<Result<Unit>> = callbackFlow {
        send(Result.Loading())

        try {
            socket.emit("leaveRoom", data)

            socket.disconnect()
            trySend(Result.Success(Unit))
        } catch (e: Exception){
            trySend(Result.Error(e))
        }

        awaitClose {
            channel.close()
        }
    }

    override suspend fun sendMessage(data: JSONObject): Flow<Result<Unit>> = callbackFlow{
        send(Result.Loading())

        try {
            socket.emit("sendMessage", data)

            trySend(Result.Success(Unit))
        } catch (e: Exception){
            trySend(Result.Error(e))
        }

        awaitClose {
            channel.close()
        }
    }

    override suspend fun receiveMessage(): Flow<Result<MessageVO>> = callbackFlow{
        send(Result.Loading())

        try {
            socket.on("receiveMessage", Emitter.Listener {
                kotlin.runCatching {
                    val data = it[0] as JSONObject
                    Log.d("TEST", data.toString())
                    return@runCatching Gson().fromJson(data.toString(), MessageVO::class.java)
                }.onSuccess {it ->
                    trySend(Result.Success(it))
                }.onFailure {  it ->
                    trySend(Result.Error(Exception(it)))
                }
            })

        } catch (e: Exception){
            trySend(Result.Error(e))
        }

        awaitClose {
            channel.close()
        }
    }

    override fun uploadImage(path: String, userId: String): Flow<Result<String>> = callbackFlow {
        send(Result.Loading())

        val imageName = "$userId/${UUID.randomUUID()}.jpg"
        val storageRef = firebaseStorage.reference.child(imageName)

        storageRef.putFile(Uri.parse(path)).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                storageRef.downloadUrl.addOnSuccessListener {
                    trySend(Result.Success(it.toString()))
                }
            } else {
                trySend(Result.Error(task.exception))
            }
        }

        awaitClose {
            channel.close()
        }
    }

}