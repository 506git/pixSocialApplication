package com.example.domain.usecase

import androidx.paging.PagingData
import com.example.domain.core.Result
import com.example.domain.model.LawMaker
import com.example.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow


//class FetchImageList(private val repository: AppRepository) {
//    suspend operator fun invoke(): Flow<PagingData<Unit>> {
//        return repository.fetchImageList()
//    }
//}