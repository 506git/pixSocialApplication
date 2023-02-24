package com.example.domain.model

data class FriendsList (
    val friends : List<FriendInfo>
)

data class FriendInfo (
    val id: String? = "",

    val name: String? = "",

    val email: String? = null,

    val picture: String? = null
)

