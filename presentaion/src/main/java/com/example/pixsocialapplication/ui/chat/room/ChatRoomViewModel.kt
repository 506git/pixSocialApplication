package com.example.pixsocialapplication.ui.chat.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.appdata_usecase.AppDataUseCase
import com.example.domain.core.Result
import com.example.domain.model.RoomListInfo
import com.example.pixsocialapplication.utils.Config
import com.example.pixsocialapplication.utils.MutableEventFlow
import com.example.pixsocialapplication.utils.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ITEMS_PER_PAGE = 50

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val appDataUseCase: AppDataUseCase
) : ViewModel() {

    private val _getRoomList = MutableSharedFlow<List<RoomListInfo>?>()
    val getRoomList get() = _getRoomList.asSharedFlow()

    private val _eventFlow = MutableEventFlow<Event>()
    val eventFlow get() = _eventFlow.asEventFlow()

    fun getRoomListRepos() {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.getRoomList(Config.userId).collect() {
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
                        _getRoomList.emit(it.data?.result?.content ?: null)
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

/*
    val pagingData: Flow<PagingData<LibraryDataSearchList>> =
        useCase.getTestData().cachedIn(viewModelScope)
    val pagingData: Flow<PagingData<Test>> = Pager(
       config =  PagingConfig(pageSize =  ITEMS_PER_PAGE, enablePlaceholders = false),
       pagingSourceFactory = {useCase.getTestData()}
    ).flow.cachedIn(viewModelScope)  paging */

}