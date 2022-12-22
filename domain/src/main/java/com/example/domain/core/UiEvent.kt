package com.example.domain.core


sealed class UiEvent {
    data class ShowSnackbar(val message: String): UiEvent()

    data class ShowLoadingScreen(val message: String): UiEvent()
    object HideLoadingScreen: UiEvent()

    object GoMain : UiEvent()
    object GoInit : UiEvent()
    object GoLogIn : UiEvent()
}