package com.example.pixsocialapplication.di

import com.example.data.repository.dataSource.RemoteDataSource
import com.example.data.repository.dataSourceImpl.RemoteDataSourceImpl
import com.example.data.service.RemoteService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {
    @Provides
    fun provideMoviesRemoteDataSource(remoteService: RemoteService) : RemoteDataSource =
        RemoteDataSourceImpl(service = remoteService)
}