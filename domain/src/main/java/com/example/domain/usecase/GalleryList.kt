package com.example.domain.usecase

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.paging.PagingData
import com.example.domain.model.RoomInfo
import com.example.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import com.example.domain.core.Result
import com.example.domain.model.LibraryDataSearchList

class GalleryList (private val appRepository: AppRepository) {
    operator fun invoke(): Flow<PagingData<Uri>> {
        Log.d("start->", "usecase")
        return appRepository.galleryList()
    }
}
