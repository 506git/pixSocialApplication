package com.example.data.mapper


import com.example.data.model.TestRes
import com.example.data.model.ContentsRes
import com.example.data.model.LibraryDataSearchListRes

import com.example.domain.model.Test
import com.example.domain.model.Contents
import com.example.domain.model.LibraryDataSearchList
import timber.log.Timber

object TestMapper {

    fun mapperToTest(testResponse: TestRes): Test {
        return Test(
            contents = mapperToContents(testResponse.contents)
//                .map {
//                mapperToContents(it)
//            }
        )
    }

    fun mapperToContents(contentsRes : ContentsRes): Contents{
//        Timber.tag("OkHttp").d("testRes ${contentsRes.libraryDataSearchList}" )
        return Contents(
            totalCount = contentsRes.totalCount,
            totalPage = contentsRes.totalCount,
            libraryDataSearchList = contentsRes.libraryDataSearchList.map {
                mapperToLibraryData(it)
            }
        )
    }

//    fun mapperToContents(contentsRes : ContentsRes): Contents{
//        Timber.tag("OkHttp").d("testRes ${contentsRes.libraryDataSearchList}" )
//        return Contents(
//            totalCount = contentsRes.totalCount,
//            totalPage = contentsRes.totalCount,
//            libraryDataSearchList = contentsRes.libraryDataSearchList.map {
//                mapperToLibraryData(it)
//            }
//        )
//    }

    fun mapperToLibraryData(libraryRes : LibraryDataSearchListRes):LibraryDataSearchList{
        return LibraryDataSearchList(
            bookKey = libraryRes.bookKey,
            libraryCode =  libraryRes.libraryCode,
            libraryName = libraryRes.libraryName,
            bookTitle = libraryRes.bookTitle,
            bookThumbnailURL = libraryRes.bookThumbnailURL
        )
    }
}