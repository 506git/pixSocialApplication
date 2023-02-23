package com.example.data.dto

import com.example.data.model.LibraryDataSearchListRes
import com.google.gson.annotations.SerializedName

data class FriendsList (
    @SerializedName("friends")
    val friends : FriendsRes

)

data class FriendsRes (
    @SerializedName("_id")
    private val id: String? = "",

    @SerializedName("name")
    private val name: String? = "",

    @SerializedName("email")
    private val email: String? = null,

    @SerializedName("picture")
    private val picture: String? = null
)

