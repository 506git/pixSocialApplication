package com.example.domain.usecase

import android.util.Log
import androidx.paging.PagingData
import com.example.domain.model.LibraryDataSearchList
import com.example.domain.model.Test
import com.example.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class GetTestDataUseCase (private val appRepository: AppRepository) {
    operator fun invoke(): Flow<PagingData<LibraryDataSearchList>> {
        return appRepository.getTestData()
    }
}