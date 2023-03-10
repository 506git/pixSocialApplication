package com.example.pixsocialapplication.ui.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.appdata_usecase.AppDataUseCase
import com.example.domain.core.Result
import com.example.domain.core.UiEvent
import com.example.domain.database_usecase.DatabaseUseCase
import com.example.domain.model.SignInState
import com.example.domain.usecase.UseCase
import com.example.pixsocialapplication.utils.Event
import com.example.pixsocialapplication.utils.MutableEventFlow
import com.example.pixsocialapplication.utils.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val useCase: UseCase,
    private val appDataUseCase: AppDataUseCase,
    private val databaseUseCase: DatabaseUseCase
) : ViewModel() {

    private var _state = MutableSharedFlow<SignInState>()
    val state = _state.asSharedFlow()

    private val _skipIntro = MutableSharedFlow<Boolean>()
    val skipIntro get() = _skipIntro.asSharedFlow()

    private val _eventFlow = MutableEventFlow<Event>()
    val eventFlow get() =  _eventFlow.asEventFlow()

    fun signInGoogleAutoLogIn() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.googleAutoLogIn().collect() {
                when (it) {
                    is Result.Error -> {
                        event(Event.OffLine(false))
                        event(Event.ShowToast(it.exception.toString()))
                        _state.emit(SignInState(isGoogleLoading = true, launchGoogleSignIn = true))
                        initUserInfoUpdateDB()
                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        _state.emit(SignInState(isGoogleLoading = true, launchGoogleSignIn = true, databaseInit= false))
                    }
                }
            }
        }
    }

    private fun event(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun signInWithGoogleIdToken(idToken: String) {
        viewModelScope.launch(IO) {
            useCase.signInWithGoogleIdToken(idToken).collect() {
                when (it) {
                    is Result.Error -> {
                        viewModelScope.launch {
                            event(Event.OffLine(false))
                            event(Event.ShowToast(it.exception.toString()))
                            _state.emit(SignInState(isGoogleLoading = true, launchGoogleSignIn = true))
                            initUserInfoUpdateDB()
                        }
                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        _state.emit(SignInState(isGoogleLoading = true, launchGoogleSignIn = true))
                        initUserInfoUpdateDB()
                    }
                }
            }
        }
    }

    fun initUserInfoUpdateDB() {
        viewModelScope.launch(IO) {
            useCase.initUserProfileInfo().collect() {
                when (it) {
                    is Result.Error -> {

                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        initUserInfoUpdateLocalDB()
                    }
                }
            }
        }
    }

    fun initUserInfoUpdateLocalDB() {
        viewModelScope.launch(IO) {
            databaseUseCase.insertUserInfo().collect() {
                when (it) {
                    is Result.Error -> {

                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        _state.emit(SignInState(isGoogleLoading = true, launchGoogleSignIn = true,  databaseInit = true))
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