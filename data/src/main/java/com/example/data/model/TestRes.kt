package com.example.data.model

import com.google.gson.annotations.SerializedName

data class TestRes (
    @SerializedName("Contents")
    val contents : ContentsRes
)

data class ContentsRes(
    @SerializedName("TotalCount")
    val totalCount : String,

    @SerializedName("TotalPage")
    val totalPage : String,

    @SerializedName("LibraryDataSearchList")
    val libraryDataSearchList : List<LibraryDataSearchListRes>,
)

data class LibraryDataSearchListRes (
    @SerializedName("BookKey")
    val bookKey : String,

    @SerializedName("LibraryCode")
    val libraryCode : String,

    @SerializedName("BookTitle")
    val bookTitle : String,

    @SerializedName("BookThumbnailURL")
    val bookThumbnailURL : String,

    @SerializedName("LibraryName")
    val libraryName : String
)