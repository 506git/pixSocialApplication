package com.example.pixsocialapplication.di

import android.content.Context
import com.example.data.repository.AppRepositoryImpl
import com.example.data.repository.DatabaseRepositoryImpl
import com.example.data.repository.dataSource.GalleryDataSource
import com.example.data.repository.dataSource.TestRemoteDataSource
import com.example.data.service.PushService
import com.example.domain.preferences.Preferences
import com.example.domain.repository.AppRepository
import com.example.domain.repository.DatabaseRepository
import com.example.domain.usecase.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideRepository(
    ): DatabaseRepository {
        return DatabaseRepositoryImpl()
    }

    @Provides
    fun provideUseCases(
        repository: DatabaseRepository
    ): DatabaseUseCase {
        return DatabaseUseCase()
    }
}