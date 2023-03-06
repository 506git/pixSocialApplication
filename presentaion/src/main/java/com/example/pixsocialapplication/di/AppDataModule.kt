package com.example.pixsocialapplication.di

import android.content.Context
import com.example.data.repository.AppDataRepositoryImpl
import com.example.data.repository.dataSource.RemoteDataSource
import com.example.data.service.PushService
import com.example.domain.appdata_usecase.*
import com.example.domain.preferences.Preferences
import com.example.domain.repository.AppDataRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.socket.client.Socket
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppDataModule {

    @Provides
    @Singleton
    fun provideRepository(
        auth: FirebaseAuth,
        testRemoteSource: RemoteDataSource,
        @ApplicationContext appContext : Context,
        pushService: PushService,
        preferences: Preferences,
        socket: Socket
    ): AppDataRepository {
        return AppDataRepositoryImpl(auth, testRemoteSource, appContext, pushService, preferences, socket)
    }

    @Provides
    fun provideUseCases(
        repository: AppDataRepository,
    ): AppDataUseCase {
        return AppDataUseCase(
            googleAutoLogIn = GoogleAutoLogin(repository),
            signInWithGoogleIdToken = SignInWithGoogleIdToken(repository),
            addFriends = AddFriend(repository),
            getFriends = GetFriendsList(repository),
            chatRoomStart = ChatRoomStart(repository),
            getRoomList = GetRoomList(repository),
            joinRoom = JoinRoom(repository)
        )
    }

}