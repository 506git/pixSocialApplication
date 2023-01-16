package com.example.pixsocialapplication.di

import com.example.data.db.UserDao
import com.example.data.db.UserDatabase
import com.example.data.repository.DatabaseRepositoryImpl
import com.example.domain.database_usecase.DatabaseUseCase
import com.example.domain.database_usecase.GetUserInfo
import com.example.domain.database_usecase.InsertUserInfo
import com.example.domain.repository.DatabaseRepository
import com.example.domain.usecase.*
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideRepository(
        userDao: UserDao,
        auth: FirebaseAuth
    ): DatabaseRepository {
        return DatabaseRepositoryImpl(userDao, auth)
    }

    @Provides
    fun provideUseCases(
        repository: DatabaseRepository
    ): DatabaseUseCase {
        return DatabaseUseCase(
            getUserInfo = GetUserInfo(repository),
            insertUserInfo = InsertUserInfo(repository)
        )
    }
}