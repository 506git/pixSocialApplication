package com.example.pixsocialapplication.ui

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import com.example.domain.appdata_usecase.AppDataUseCase
import com.example.domain.core.Result
import com.example.domain.usecase.UseCase
import com.example.pixsocialapplication.utils.*
import com.example.pixsocialapplication.utils.flowLib.MutableEventFlow
import com.example.pixsocialapplication.utils.flowLib.asEventFlow
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: UseCase,
    private val appDataUseCase: AppDataUseCase
) : ViewModel() {

    private val _eventFlow = MutableEventFlow<MainEvent>()
    val eventFlow get() = _eventFlow.asEventFlow()

    fun findChatUser(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.addFriends(userId).collect() {
                when (it) {
                    is Result.Error -> {
                        event(MainEvent.ShowToast(it.exception.toString()))
                        event(MainEvent.BottomBehavior(BottomSheetBehavior.STATE_HIDDEN))
                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        event(MainEvent.BottomBehavior(BottomSheetBehavior.STATE_HIDDEN))
                    }
                }
            }
        }
    }

    fun setAppbarTitle(title: String, desc: String) {
        viewModelScope.launch(Dispatchers.IO) {
            event(MainEvent.AppbarTitle(title.toString()))
        }
    }

    fun navAppbarTitle(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            event(MainEvent.AppbarTitle(title.toString()))
        }
    }

    fun setBottomVisible(visible: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            event(MainEvent.BottomBehavior(visible))
        }
    }

    fun setToken(key: String, token: String) {
        useCase.setStringPreferences(key, token)
    }

    fun getToken(key: String): String {
        return useCase.getStringPreferences(key)
    }

    fun updateUserFcmToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.updatePushToken(Config.userId, token).collect()
        }
    }

    private suspend fun event(event: MainEvent) {
        _eventFlow.emit(event)
    }
}