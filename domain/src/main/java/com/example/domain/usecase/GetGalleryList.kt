package com.example.domain.usecase

import android.graphics.Bitmap
import android.net.Uri
import com.example.domain.model.RoomInfo
import com.example.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import com.example.domain.core.Result

class GetGalleryList (private val appRepository: AppRepository) {
    operator fun invoke():  Flow<Result<List<Uri>>> {
        return appRepository.getGalleryList()
    }
}
