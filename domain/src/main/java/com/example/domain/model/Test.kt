package com.example.domain.model


data class Test(
//    val contents: List<Contents>
    val contents: Contents
)

data class Contents(
    val totalCount: String,
    val totalPage: String,
    val libraryDataSearchList: List<LibraryDataSearchList>,
)

data class LibraryDataSearchList(
    val bookKey : String,
    val libraryCode: String,
    val bookTitle: String,
    val bookThumbnailURL: String,
    val libraryName: String
)