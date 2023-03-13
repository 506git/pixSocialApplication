package com.example.data.repository.dataSourceImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.dto.*
import com.example.data.model.RoomIdDTO
import com.example.data.model.UserDTO
import com.example.data.paging.TestPagingSource
import com.example.data.repository.dataSource.RemoteDataSource
import com.example.data.service.RemoteService
import com.example.domain.model.LibraryDataSearchList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val service: RemoteService): RemoteDataSource {

    override fun getTestData(): Flow<PagingData<LibraryDataSearchList>> {
        return Pager(
            config = PagingConfig(pageSize = 50),
            pagingSourceFactory = { TestPagingSource(service) }
        ).flow
    }

    override suspend fun googleLogin(accessToken: String): ApiResponse<UserInfoDTO> {
        return service.googleLogin(accessToken)
    }

    override suspend fun getUserInfo(uid: String) {
        return service.getUserInfo(uid)
    }

    override suspend fun updatePushToken(userId : String, token: String) : ApiResponse<Unit>{
        return service.updatePushToken(UserPushDTO(userId = userId, fcmToken = token))
    }

    override suspend fun addFriendsAdd(friendsAddDTO: FriendsAddDTO) {
        return service.addFriend(friendsAddDTO)
    }

    override suspend fun getFriendsList(id: UserDTO): ApiResponse<List<FriendInfoDTO>> {
        return service.getFriendsList(id)
    }

    override suspend fun createRoom(createRoom: CreateRoomDTO): ApiResponse<RoomListInfoDTO> {
        return service.createRoom(createRoom)
    }

    override suspend fun getRoomList(user: UserDTO): ApiResponse<List<RoomListInfoDTO>> {
        return service.getRoomList(user)
    }

    override suspend fun getChatList(roomId: String): ApiResponse<List<ChatListDTO>> {
        return service.getChatList(RoomIdDTO(roomId = roomId))
    }
}