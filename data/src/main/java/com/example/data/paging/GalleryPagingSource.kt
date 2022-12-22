package com.example.data.paging

import androidx.paging.*
import com.example.data.mapper.TestMapper

import com.example.data.service.TestService
import com.example.domain.model.Contents
import com.example.domain.model.LibraryDataSearchList
import com.example.domain.model.Test
import com.google.gson.Gson
import kotlinx.coroutines.delay
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class GalleryPagingSource @Inject constructor(private val service: TestService)
    : PagingSource<Int, LibraryDataSearchList>()  {

    override fun getRefreshKey(state: PagingState<Int, LibraryDataSearchList>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LibraryDataSearchList> {
        val page = params.key ?: 1
        return try {
            delay(500)

            val items = service.getTest(page = page).contents.libraryDataSearchList.map {
                TestMapper.mapperToLibraryData(it)
            }

            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + (params.loadSize / 10)
            )

        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}