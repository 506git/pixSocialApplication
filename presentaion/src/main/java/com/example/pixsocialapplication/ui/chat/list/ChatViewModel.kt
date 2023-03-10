package com.example.pixsocialapplication.ui.chat.list

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.appdata_usecase.AppDataUseCase
import com.example.domain.core.Result
import com.example.domain.usecase.UseCase
import com.example.domain.vo.ChatListVO
import com.example.pixsocialapplication.ui.chat.list.testData.ArticleRepository
import com.example.pixsocialapplication.utils.*
import com.example.pixsocialapplication.utils.flowLib.MutableEventFlow
import com.example.pixsocialapplication.utils.flowLib.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val useCase: UseCase,
    private val appDataUseCase: AppDataUseCase
) : ViewModel() {

    private val _getRoomChatList = MutableSharedFlow<List<ChatListVO>?>()
    val getRoomChatList get() = _getRoomChatList.asSharedFlow()

    private val _eventFlow = MutableEventFlow<MessageEvent>()
    val eventFlow get() = _eventFlow.asEventFlow()

    val pagingData: Flow<PagingData<Uri>> = useCase.galleryList().cachedIn(viewModelScope)

    private suspend fun event(event: MessageEvent) {
        _eventFlow.emit(event)
    }

    fun joinRoom(data: JSONObject) {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.joinRoom(data).collect() { }
        }
    }

    fun leaveRoom(data: JSONObject) {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.leaveRoom(data).collect() { }
        }
    }

    fun sendMessage(data: JSONObject) {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.sendMessage(data).collect() {
                when (it) {
                    is Result.Error -> {
                        event(MessageEvent.Loading(false))
                        event(MessageEvent.ShowToast(it.exception.toString()))
                    }
                    is Result.Loading -> {
                        event(MessageEvent.Loading(true))
                    }
                    is Result.Success -> {
                        event(MessageEvent.Loading(false))
                    }
                }
            }
        }
    }

    fun receiveMessage() {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.receiveMessage().collect() {
                when (it) {
                    is Result.Error -> {
                        event(MessageEvent.ShowToast(it.exception.toString()))
                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        event(
                            MessageEvent.AddMessage(
                                ChatListVO(
                                    user_id = it.data?.userId.toString(),
                                    message_type = it.data?.messageType.toString(),
                                    message_body = it.data?.messageBody.toString(),
                                    message_sender = if (it.data?.userId.toString() == Config.userId) "me" else "you"
                                )
                            )
                        )
                    }
                }
            }
        }
    }

//    fun removeChat(messageId: String, roomId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            useCase.removeChat(messageId, roomId).collect() {
//                when (it) {
//                    is Result.Error -> {
//                        withContext(Dispatchers.Main) {
//                            _loadingState.value = false
//                        }
//                    }
//                    is Result.Loading -> {
//                        withContext(Dispatchers.Main) {
//                            _loadingState.value = true
//                        }
//                    }
//                    is Result.Success -> {
//                        withContext(Dispatchers.Main) {
//                            _loadingState.value = false
//                        }
//                    }
//
//                }
//            }
//        }
//    }

    fun getRoomChatList(roomId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.getChatList(roomId).collect() {
                when (it) {
                    is Result.Error -> {
                        event(MessageEvent.Loading(false))
                        event(MessageEvent.ShowToast(it.exception.toString()))
                    }
                    is Result.Loading -> {
                        event(MessageEvent.Loading(true))
                    }
                    is Result.Success -> {
                        event(MessageEvent.Loading(false))
                        _getRoomChatList.emit(
                            if (it.data?.isEmpty() == true){
                                null
                            } else {
                                it.data?.map { it ->
                                    it.copy(
                                        createdAt = DateUtils.convertDate(it.createdAt),
                                        message_sender = if (it.user_id == Config.userId) "me" else "you"
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    fun sendImageMessage(message: String, userId: String, roomId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.uploadImage(message, userId).collect() {
                when (it) {
                    is Result.Error -> {
                        event(MessageEvent.Loading(false))
                    }
                    is Result.Loading -> {
                        event(MessageEvent.Loading(true))
                    }
                    is Result.Success -> {
                        event(MessageEvent.Loading(false))
                        sendMessage(JSONObject().apply {
                            put("roomId", roomId)
                            put("userId", userId)
                            put("messageBody", it.data.toString())
                            put("messageType", "image")
                        })
                    }
                }
            }
        }
    }
}