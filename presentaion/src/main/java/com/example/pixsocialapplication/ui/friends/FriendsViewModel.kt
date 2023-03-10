package com.example.pixsocialapplication.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.appdata_usecase.AppDataUseCase
import com.example.domain.core.Result
import com.example.domain.model.FriendInfo
import com.example.pixsocialapplication.utils.Config
import com.example.pixsocialapplication.utils.flowLib.MutableEventFlow
import com.example.pixsocialapplication.utils.flowLib.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor( private val appDataUseCase: AppDataUseCase ) : ViewModel() {

    private val _getFriendList = MutableSharedFlow<List<FriendInfo>?>()
    val getFriendList get() = _getFriendList.asSharedFlow()

    private val _eventFlow = MutableEventFlow<Event>()
    val eventFlow get() = _eventFlow.asEventFlow()

    fun getFriendsListRepos() {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.getFriends(Config.userId).collect() {
                when (it) {
                    is Result.Error -> {
                        event(Event.ShowToast(it.exception.toString()))
                        event(Event.Loading(false))
                    }
                    is Result.Loading -> {
                        event(Event.Loading(true))
                    }
                    is Result.Success -> {
                        event(Event.Loading(false))

                        if (it.data?.result?.content?.isEmpty() == true) {
                            _getFriendList.emit(null)
                        } else _getFriendList.emit(it.data?.result?.content)

                    }
                }
            }
        }
    }

    private suspend fun event(event: Event) {
        _eventFlow.emit(event)
    }

    sealed class Event {
        data class ShowToast(val text: String) : Event()
        data class OffLine(val state : Boolean) : Event()
        data class Loading(val visible : Boolean) : Event()
    }
}