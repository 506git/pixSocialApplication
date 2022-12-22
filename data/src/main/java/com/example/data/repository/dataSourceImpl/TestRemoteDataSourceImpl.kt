package com.example.data.repository.dataSourceImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.model.TestRes
import com.example.data.paging.TestPagingSource
import com.example.data.repository.dataSource.TestRemoteDataSource
import com.example.data.service.TestService
import com.example.domain.model.LibraryDataSearchList
import com.example.domain.model.Test
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class TestRemoteDataSourceImpl @Inject constructor(private val service: TestService): TestRemoteDataSource {

    override fun getTestData(): Flow<PagingData<LibraryDataSearchList>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { TestPagingSource(service) }
        ).flow
    }

}