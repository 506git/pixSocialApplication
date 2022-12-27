package com.example.domain.usecase

class UseCase(
    val saveIntroViewStatus: IntroViewStatus,
    val loadIntroViewStatus: LoadIntroViewStatus,
    val setStringPreferences: SetStringPreferences,

    val getStringPreferences: GetStringPreferences,
    val signUp: SignUp,
    val signIn: SignIn,
    val signInWithGoogleIdToken: SignInWithGoogleIdToken,
    val googleAutoLogIn: GoogleAutoLogIn,
    val googleSignIn : GoogleSignIn,

    val updateUserProfile: UpdateUserProfile,
    val getTestData : GetTestDataUseCase,
    val initUserProfileInfo : InitUserProfileInfo,
    val updateUserFcmToken: UpdateUserFcmToken,

    val getRoomInfo : GetRoomInfoUseCase,

    val findUserId : FindUserId,
    val getRoomChat : GetRoomChat,

    val sendChat : SendChat,
    val sendImage : SendImage,

    val getGalleryList : GetGalleryList,
    val galleryList : GalleryList
)
