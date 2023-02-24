package com.example.data.mapper

import com.example.data.model.LocalUser

object UserMapper {
    fun mapperToUserInfo(localUser: LocalUser): com.example.domain.model.User {
        return  com.example.domain.model.User(
            uid = localUser.uid,
            displayName = localUser.displayName.toString(),
            email = localUser.email,
            desc = localUser.desc.toString(),
            imageUrl = localUser.imageUrl.toString()
        )
    }

    fun mapperToUserInfo(user : com.example.domain.model.User): LocalUser {
        return  LocalUser(
            uid = user.uid,
            displayName = user.displayName,
            email = user.email,
            desc = user.desc,
            imageUrl = user.imageUrl
        )
    }

//    fun mapperToFriendsList(friendsList: FriendsList): com.example.domain.model.FriendsList {
//        val json = Gson().toJson(friendsList)
//        val test = Gson().fromJson(json, FriendsList::class.java)
//        return  com.example.domain.model.FriendsList(
//            friends = test.friends
//        )
//    }
//
//    fun mapperToUserInfo(friendsList : com.example.domain.model.FriendsList): FriendsList {
//        return  FriendsList(
//
//        )
//    }
//
//    fun mapperToFriendInfo(friendsList: FriendInfo): com.example.domain.model.FriendInfo {
//        val json = Gson().toJson(friendsList)
//        val test = Gson().fromJson(json, FriendsList::class.java)
//        return  com.example.domain.model.FriendInfo(
//            friends = test.friends
//        )
//    }
//
//    fun mapperToFriendInfo(friendsList : com.example.domain.model.FriendInfo): FriendInfo {
//        return  FriendInfo(
//
//        )
//    }
}