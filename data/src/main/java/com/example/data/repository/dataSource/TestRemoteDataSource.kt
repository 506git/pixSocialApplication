package com.example.data.repository.dataSource

import androidx.paging.PagingData

import com.example.data.model.TestRes
import com.example.domain.model.LibraryDataSearchList
import com.example.domain.model.Test
import kotlinx.coroutines.flow.Flow

interface TestRemoteDataSource {
     fun getTestData(): Flow<PagingData<LibraryDataSearchList>>
}