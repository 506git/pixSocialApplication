package com.example.pixsocialapplication.ui.chat.list

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Config
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.appdata_usecase.AppDataUseCase
import com.example.domain.core.Result
import com.example.domain.usecase.UseCase
import com.example.domain.model.RoomChat
import com.example.domain.vo.ChatListVO
import com.example.pixsocialapplication.ui.chat.list.testData.ArticleRepository
import com.example.pixsocialapplication.utils.Config
import com.example.pixsocialapplication.utils.DLog
import com.example.pixsocialapplication.utils.DateUtils
import com.google.android.gms.common.util.DataUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

private const val ITEMS_PER_PAGE = 50

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val useCase: UseCase,
    private val appDataUseCase: AppDataUseCase
) : ViewModel() {

    //    val pagingData = useCase.fetchImageList.cachedIn(viewModelScope)
    private var repository = ArticleRepository()

    private val _getRoomChatList = MutableLiveData<List<ChatListVO>?>()
    val getRoomChatList: LiveData<List<ChatListVO>?> get() = _getRoomChatList

    private val _getGalleryList = MutableLiveData<List<Uri>?>()
    val getGalleryList: LiveData<List<Uri>?> get() = _getGalleryList

    private val _loadingState = MutableLiveData<Boolean>(false)
    val loadingState: LiveData<Boolean> get() = _loadingState

    private val _itemSelectedPos = MutableLiveData<String>()
    val itemSelectedPos: LiveData<String> get() = _itemSelectedPos

    private val _chat = MutableSharedFlow<ChatListVO>()
    val chat get() = _chat.asSharedFlow()


    fun setItemSelected(uri: String) {
        _itemSelectedPos.value = uri.toString()
    }

    val pagingData: Flow<PagingData<Uri>> =
        useCase.galleryList().cachedIn(viewModelScope)

//    val items: Flow<PagingData<Article>> = Pager(
//        config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
//        pagingSourceFactory = { repository.articlePagingSource() }
//    ).flow.cachedIn(viewModelScope)
//
//    val pagingData: Flow<PagingData<LibraryDataSearchList>> = useCase.getTestData().cachedIn(viewModelScope)

//    fun updateUserProfile(userName: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            useCase.updateUserProfile(userName).collect() {
//                when (it) {
//                    is Result.Error -> {
//
//                    }
//                    is Result.Loading -> {
//
//                    }
//                    is Result.Success -> {
//
//                    }
//
//                }
//            }
//        }
//    }

    fun joinRoom(data: JSONObject) {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.joinRoom(data).collect() {
                when (it) {
                    is Result.Error -> {
                        DLog().d("error", "test")
                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        DLog().d("susceess", "test")
                    }
                }
            }
        }
    }


    fun leaveRoom(data: JSONObject) {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.leaveRoom(data).collect() {
                when (it) {
                    is Result.Error -> {
                        DLog().d("error", "test")
                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        DLog().d("susceess", "test")
                    }
                }
            }
        }
    }

    fun sendMessage(data: JSONObject) {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.sendMessage(data).collect() {
                when (it) {
                    is Result.Error -> {
                        DLog().d("error", it.exception.toString())
                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        DLog().d("susceess", "test")
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
                        DLog().d("error", it.exception.toString())
                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        _chat.emit(
                            ChatListVO(
                                user_id = it.data?.userId.toString(),
                                message_type = it.data?.messageType.toString(),
                                message_body = it.data?.messageBody.toString(),
                                message_sender = if (it.data?.userId.toString() == Config.userId) "me" else "you"
                            )
                        )
                        DLog().d("susceess", it.data.toString())
                    }
                }
            }
        }
    }

    fun removeChat(messageId: String, roomId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.removeChat(messageId, roomId).collect() {
                when (it) {
                    is Result.Error -> {
                        withContext(Dispatchers.Main) {
                            _loadingState.value = false
                        }
                    }
                    is Result.Loading -> {
                        withContext(Dispatchers.Main) {
                            _loadingState.value = true
                        }
                    }
                    is Result.Success -> {
                        withContext(Dispatchers.Main) {
                            _loadingState.value = false
                        }
                    }

                }
            }
        }
    }

//    fun getRoomChatList2(roomId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            useCase.getRoomChat(roomId).collect() {
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
//                            if(it.data?.isEmpty() == true){
//                                _getRoomChatList.value = null
//                            } else _getRoomChatList.value = it.data
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
                        withContext(Dispatchers.Main) {
                            _loadingState.value = false
                        }
                        DLog().d("error chat list -> ${it.exception.toString()}")
                    }
                    is Result.Loading -> {
                        withContext(Dispatchers.Main) {
                            _loadingState.value = true
                        }
                    }
                    is Result.Success -> {
                        withContext(Dispatchers.Main) {
                            _loadingState.value = false

                            if (it.data?.isEmpty() == true) {
                                _getRoomChatList.value = null
                            } else {

                                DLog().d(Config.userId)
                                _getRoomChatList.value = it.data?.map { it ->
                                    it.copy(
                                        createdAt = DateUtils.convertDate(it.createdAt),
                                        message_sender = if (it.user_id == Config.userId) "me" else "you"
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    fun sendMessage(message: String, roomId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.sendChat(message, roomId).collect() {
                when (it) {
                    is Result.Error -> {
                        withContext(Dispatchers.Main) {
                            _loadingState.value = false
                        }
                    }
                    is Result.Loading -> {
                        withContext(Dispatchers.Main) {
                            _loadingState.value = true
                        }
                    }
                    is Result.Success -> {
                        withContext(Dispatchers.Main) {
                            _loadingState.value = false
                        }
                    }
                }
            }
        }
    }

    fun sendImageMessage(message: String, roomId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.sendImage(message, roomId).collect() {
                when (it) {
                    is Result.Error -> {
                        withContext(Dispatchers.Main) {
                            _loadingState.value = false
                        }
                    }
                    is Result.Loading -> {
                        withContext(Dispatchers.Main) {
                            _loadingState.value = true
                        }
                    }
                    is Result.Success -> {
                        withContext(Dispatchers.Main) {
                            _loadingState.value = false
                        }
                    }
                }
            }
        }
    }

    fun getImageList() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getGalleryList().collect() {
                when (it) {
                    is Result.Error -> {
                        withContext(Dispatchers.Main) {
                            _loadingState.value = false
                        }
                    }
                    is Result.Loading -> {
                        withContext(Dispatchers.Main) {
                            _loadingState.value = true
                        }
                    }
                    is Result.Success -> {
                        withContext(Dispatchers.Main) {
                            _loadingState.value = false
                            if (it.data!!.isEmpty()) {
                                _getGalleryList.value = null
                            } else _getGalleryList.value = it.data
                        }
                    }
                }
            }
        }
    }


}