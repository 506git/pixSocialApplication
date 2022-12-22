package com.example.data.mapper

import com.example.data.model.RoomChat

object RoomChatMapper {

    fun mapperToRoomChat(roomChat: RoomChat): com.example.domain.model.RoomChat {
        return  com.example.domain.model.RoomChat(
            message = roomChat.message,
            messageId = roomChat.message_id,
            messageType = roomChat.message_type,
            messageDate = roomChat.message_date,
            messageSenderImage = roomChat.message_sender_image,
            messageSenderName = roomChat.message_sender_name
        )
    }

    fun mapperToRoomChat(roomChat: com.example.domain.model.RoomChat): RoomChat {
        return  RoomChat(
            message = roomChat.message,
            message_id = roomChat.messageId,
            message_type = roomChat.messageType,
            message_date = roomChat.messageDate.toString(),
            message_sender_image = roomChat.messageSenderImage
        )
    }
}




