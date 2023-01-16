package com.example.data.repository

import com.example.data.db.UserDao
import com.example.data.mapper.UserMapper
import com.example.domain.core.Result
import com.example.domain.model.User
import com.example.domain.repository.DatabaseRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val auth : FirebaseAuth
) : DatabaseRepository {

    override suspend fun getUserInfo(): Flow<Result<User>> = callbackFlow {
        send(Result.Loading())

        val user = UserMapper.mapperToUserInfo(userDao.getAll())
        send(Result.Success(user))

        awaitClose {
            channel.close()
        }
    }

    override suspend fun insertUserInfo(): Flow<Result<Unit>> = callbackFlow{
        send(Result.Loading())
        val user = auth.currentUser
        var userInfo: User? = null

        if(user != null){
            userInfo = User(
                uid = user.uid,
                displayName = user.displayName.toString(),
                imageUrl = user.photoUrl.toString(),
                email = user.email.toString(),
                desc = ""
            )
            userDao.insertUser(UserMapper.mapperToUserInfo(userInfo))
            trySend(Result.Success(Unit))
        } else
            trySend(Result.Error(NullPointerException()))

        awaitClose {
            channel.close()
        }

    }
}