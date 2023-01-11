package com.example.pixsocialapplication.ui.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.core.UiEvent
import com.example.domain.model.SignInState
import com.example.domain.usecase.UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.example.domain.core.Result
import com.example.pixsocialapplication.utils.DLog
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {
    private var _state = MutableLiveData(SignInState())
    val state: LiveData<SignInState> = _state

    private val _skipIntro = MutableLiveData<Boolean>()
    val skipIntro: LiveData<Boolean> = _skipIntro

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun signInGoogleAutoLogIn() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.googleAutoLogIn().collect() {
                when (it) {
                    is Result.Error -> {
                        withContext(Dispatchers.Main) {
                            _state.value = _state.value?.copy(
                                isGoogleLoading = false,
                                launchGoogleSignIn = false
                            )
                        }

                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        withContext(Dispatchers.Main) {
                            _state.value = _state.value?.copy(
                                isGoogleLoading = true,
                                launchGoogleSignIn = true,
                                databaseInit = false
                            )
                        }

                    }
                }
            }
        }
    }

    fun signInWithGoogleIdToken(idToken: String) {
        DLog().d("login start")
        viewModelScope.launch(IO) {
            useCase.signInWithGoogleIdToken(idToken).collect() {
                when (it) {
                    is Result.Error -> {

                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        withContext(Dispatchers.Main) {
                            _state.value = _state.value?.copy(
                                isGoogleLoading = true,
                                launchGoogleSignIn = true
                            )
                        }
                        initUserInfoUpdateDB()
                    }
                }
            }
        }
    }

    fun initUserInfoUpdateDB(){
        viewModelScope.launch(IO) {
            useCase.initUserProfileInfo().collect(){
                when (it){
                    is Result.Error -> {

                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        withContext(Main) {
                            _state.value = _state.value?.copy(
                                isGoogleLoading = true,
                                launchGoogleSignIn = true,
                                databaseInit = true
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateUserProfile(userName: String) {
        viewModelScope.launch(IO) {
            useCase.updateUserProfile(userName).collect() {
                when (it) {
                    is Result.Error -> {

                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {

                    }

                }
            }
        }
    }
}