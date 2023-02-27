package com.example.data.dto

import com.google.gson.annotations.SerializedName

data class FriendsList (
    @SerializedName("result")
    private val result: FriendsResultInfo,
)

data class FriendsResultInfo (
    @SerializedName("resultCode")
    private val resultCode: String = "",

    @SerializedName("resultMessage")
    private val resultMessage: String = "",

    @SerializedName("content")
    private val content: List<FriendInfo>,
)

data class FriendInfo (
    @SerializedName("_id")
    private val id: String? = "",

    @SerializedName("name")
    private val name: String? = "",

    @SerializedName("email")
    private val email: String? = null,

    @SerializedName("picture")
    private val picture: String? = null,

    @SerializedName("comment")
    private val comment: String? = null,
)

