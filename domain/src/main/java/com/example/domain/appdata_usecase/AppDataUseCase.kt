package com.example.domain.appdata_usecase


class AppDataUseCase(
    val googleAutoLogIn : GoogleAutoLogin,

    val signInWithGoogleIdToken: SignInWithGoogleIdToken,

    val updatePushToken : UpdatePushToken,

    val addFriends : AddFriend,

    val getFriends : GetFriendsList,

    val chatRoomStart : ChatRoomStart,

    val getRoomList : GetRoomList,

    val getChatList : GetChatList,

    val joinRoom : JoinRoom,

    val sendMessage: SendMessage,

    val receiveMessage: ReceiveMessage,

    val leaveRoom: LeaveRoom
)