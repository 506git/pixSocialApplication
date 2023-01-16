package com.example.data.mapper

import com.example.data.model.LocalUser
import com.example.data.model.RoomChat

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
}