package com.example.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_user")
data class LocalUser (
    @PrimaryKey val uid : String,
    @ColumnInfo(name = "display_name") val displayName : String?,
    @ColumnInfo(name = "email") val email : String,
    @ColumnInfo(name = "desc") val desc : String? = "",
    @ColumnInfo(name = "image_url") val imageUrl : String? = "",
    @ColumnInfo(name = "comment") val comment: String? = ""
)

@Entity(tableName = "friend_info")
data class LocalFriendInfo (
    @PrimaryKey val uid : String,
    @ColumnInfo(name = "display_name") val displayName : String?,
    @ColumnInfo(name = "email") val email : String,
    @ColumnInfo(name = "desc") val desc : String? = "",
    @ColumnInfo(name = "image_url") val imageUrl : String? = "",
    @ColumnInfo(name = "comment") val comment: String? = ""
)

@Entity(tableName = "room_info")
data class LocalRoomInfo (
    @PrimaryKey val room_id : String,
    @ColumnInfo(name = "room_img") val roomImg : String?,
    @ColumnInfo(name = "room_title") val roomTitle : String,
    @ColumnInfo(name = "room_name") val roomName : String? = "",
    @ColumnInfo(name = "image_url") val imageUrl : String? = "",
)

@Entity(tableName = "chat_info")
data class LocalChatInfo (
    @PrimaryKey val uid : String,
    @ColumnInfo(name = "chat_id") val chat_id : String?,
    @ColumnInfo(name = "user_id") val user_id : String,
    @ColumnInfo(name = "message_profile") val message_profile : String? = "",
    @ColumnInfo(name = "message_name") val message_name : String? = "",
    @ColumnInfo(name = "message_id") val message_id: String? = "",
    @ColumnInfo(name = "message_type") val message_type : String? = "",
    @ColumnInfo(name = "createdAt") val createdAt : String? = "",
    @ColumnInfo(name = "message_sender") val message_sender: String? = ""
)
