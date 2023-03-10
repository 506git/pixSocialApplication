package com.example.pixsocialapplication.di

import android.content.Context
import com.example.data.repository.AppDataRepositoryImpl
import com.example.data.repository.dataSource.RemoteDataSource
import com.example.data.service.PushService
import com.example.domain.appdata_usecase.AddFriend
import com.example.domain.appdata_usecase.AppDataUseCase
import com.example.domain.appdata_usecase.GetFriendsList
import com.example.domain.repository.AppDataRepository
import com.example.domain.usecase.*
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    ): AppDataRepository {
        return AppDataRepositoryImpl(auth, testRemoteSource, appContext, pushService)
    }

    @Provides
    fun provideUseCases(
        repository: AppDataRepository,
    ): AppDataUseCase {
        return AppDataUseCase(
            addFriends = AddFriend(repository),
            getFriends = GetFriendsList(repository)
        )
    }

}