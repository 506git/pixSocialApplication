package com.example.data.paging

import android.net.Uri
import androidx.paging.*
import com.example.data.mapper.TestMapper
import com.example.data.service.GalleryService

import com.example.data.service.RemoteService
import com.example.domain.core.Result
import com.example.domain.model.Contents
import com.example.domain.model.LibraryDataSearchList
import com.example.domain.model.Test
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

private const val STARTING_KEY = 0
private const val LOAD_DELAY_MILLIS = 200L

@OptIn(ExperimentalPagingApi::class)
class GalleryPagingSource @Inject constructor(private val service: GalleryService)
    : PagingSource<Int, Uri>()  {

    override fun getRefreshKey(state: PagingState<Int, Uri>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Uri> {
        val page = params.key ?: STARTING_KEY
        return try {

            val items = service.galleryList(perPage = 60, start = page)
            delay(LOAD_DELAY_MILLIS)
            LoadResult.Page(
                data = items,
                prevKey = if (page <= 1) null else page - 60,
                nextKey = if (items.isEmpty()) null else page + 60
            )

        } catch (e: Exception) {
            Timber.tag("start->").d("errr : ${e.toString()}")
            return LoadResult.Error(e)
        }
    }
}