package com.example.domain.appdata_usecase


class AppDataUseCase(
    val googleAutoLogIn : GoogleAutoLogin,

    val signInWithGoogleIdToken: SignInWithGoogleIdToken,

    val addFriends : AddFriend,

    val getFriends : GetFriendsList
)