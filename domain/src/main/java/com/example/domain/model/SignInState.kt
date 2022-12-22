package com.example.domain.model

data class SignInState (
    val userStatus: UserStatus? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isGoogleLoading: Boolean = false,
    val launchGoogleSignIn: Boolean = false,
    val databaseInit : Boolean = false
)