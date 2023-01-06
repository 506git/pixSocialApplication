package com.example.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import com.example.domain.core.Result
import com.example.domain.model.*

interface AppRepository {
    suspend fun signUp(email: String, password: String): Flow<Result<Unit>>
    suspend fun signIn(email: String, password: String): Flow<Result<Unit>>
    suspend fun signInWithCredential(idToken: String): Flow<Result<Unit>>
    fun signOut()
    suspend fun googleAutoLogIn(): Flow<Result<Unit>>
    suspend fun googleSignIn() : Flow<Result<Unit>>

    suspend fun updateUserProfileToFirebaseDb(userName: String): Flow<Result<Unit>>
    fun getTestData() : Flow<PagingData<LibraryDataSearchList>>
    fun getRoomInfo() : Flow<Result<List<RoomInfo>>>
    suspend fun initUserProfileInfo(): Flow<Result<Unit>>
    suspend fun addUserId(userId: String): Flow<Result<Unit>>
    fun updateUserFcmToken(token: String)

    fun getRoomChat(roomId : String) : Flow<Result<List<RoomChat>>>
    suspend fun sendChat(message: String, roomId : String) : Flow<Result<Unit>>

    suspend fun sendImage(message: String, roomId : String) : Flow<Result<Unit>>
    fun getGalleryList() : Flow<Result<List<Uri>>>

    fun galleryList() : Flow<PagingData<Uri>>

    suspend fun removeChat(messageId: String, roomId : String) : Flow<Result<Unit>>
//    suspend fun fetchImageList() : Flow<PagingData<LawMaker>>
}