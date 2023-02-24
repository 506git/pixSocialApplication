package com.example.data.repository.dataSourceImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.dto.FriendsAddDTO
import com.example.data.dto.FriendsList
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

    override suspend fun googleLogin(accessToken: String) {
        return service.googleLogin(accessToken)
    }

    override suspend fun getUserInfo(uid: String) {
        return service.getUserInfo(uid)
    }

    override suspend fun addFriendsAdd(friendsAddDTO: FriendsAddDTO) {
        return service.addFriend(friendsAddDTO)
    }

    override suspend fun getFriendsList(id: UserDTO): FriendsList {
        return service.getFriendsList(id)
    }


}