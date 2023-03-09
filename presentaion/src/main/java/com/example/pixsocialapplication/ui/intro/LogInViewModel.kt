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
import com.example.pixsocialapplication.utils.*
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

    private val _eventFlow = MutableEventFlow<Event>()
    val eventFlow get() = _eventFlow.asEventFlow()

    fun signInGoogleAutoLogIn() {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.googleAutoLogIn().collect() {
                when (it) {
                    is Result.Error -> {
                        event(Event.OffLine(false))
                        event(Event.ShowToast(it.exception.toString()))
                        event(Event.GoMain(false))
                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        DLog().d(it.data.toString())
                        setID(Config._ID, it.data!!._id)
                        event(Event.GoMain(true))
//                        initUserInfoUpdateLocalDB()
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
            appDataUseCase.signInWithGoogleIdToken(idToken).collect() {
                when (it) {
                    is Result.Error -> {
                        event(Event.OffLine(false))
                        event(Event.ShowToast(it.exception.toString()))
                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        event(Event.GoMain(true))
                        setID(Config._ID, it.data!!._id)
//                        initUserInfoUpdateLocalDB()
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
//
                    }
                }
            }
        }
    }

    private fun setID(key: String, token: String) {
        useCase.setStringPreferences(key, token)
    }

    sealed class Event {
        data class ShowToast(val text: String) : Event()
        data class OffLine(val state : Boolean) : Event()
        data class GoMain(val state : Boolean) : Event()
    }
}