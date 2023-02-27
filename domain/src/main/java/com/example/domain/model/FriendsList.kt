package com.example.domain.model

data class FriendsList (
    val result: FriendsResultInfo,
)

data class FriendsResultInfo (
    val resultCode: String = "",

    val resultMessage: String = "",

    val content: List<FriendInfo>,
)

data class FriendInfo (
    val id: String? = "",

    val name: String? = "",

    val email: String? = null,

    val picture: String? = null,

    val comment: String? = null
)

