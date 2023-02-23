package com.example.data.repository.dataSourceImpl

import android.net.Uri
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.paging.GalleryPagingSource
import com.example.data.repository.dataSource.GalleryDataSource
import com.example.data.service.GalleryService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GalleryDataSourceImpl @Inject constructor(private val service: GalleryService): GalleryDataSource {

    override fun getGalleryData(): Flow<PagingData<Uri>> {
        Log.d("start->","impl")
        return Pager(
            config = PagingConfig(pageSize = 60),
            pagingSourceFactory = { GalleryPagingSource(service) }
        ).flow
    }

}