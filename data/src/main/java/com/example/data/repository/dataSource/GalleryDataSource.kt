package com.example.data.repository.dataSource

import android.net.Uri
import androidx.paging.PagingData

import com.example.data.model.TestRes
import com.example.domain.model.LibraryDataSearchList
import com.example.domain.model.Test
import kotlinx.coroutines.flow.Flow

interface GalleryDataSource {
     fun getGalleryData(): Flow<PagingData<Uri>>
}