package com.example.pixsocialapplication.di

import com.example.data.repository.dataSource.TestRemoteDataSource
import com.example.data.repository.dataSourceImpl.TestRemoteDataSourceImpl
import com.example.data.service.TestService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {
    @Provides
    fun provideMoviesRemoteDataSource(testService: TestService) : TestRemoteDataSource =
        TestRemoteDataSourceImpl(service = testService)
}