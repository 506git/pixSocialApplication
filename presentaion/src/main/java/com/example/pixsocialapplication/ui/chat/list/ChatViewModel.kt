package com.example.pixsocialapplication.ui.chat.list

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.core.Result
import com.example.domain.usecase.UseCase
import com.example.domain.model.RoomChat
import com.example.pixsocialapplication.ui.chat.list.testData.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val ITEMS_PER_PAGE = 50

@HiltViewModel
class ChatViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {

    //    val pagingData = useCase.fetchImageList.cachedIn(viewModelScope)
    private var repository = ArticleRepository()

    private val _getRoomChatList = MutableLiveData<List<RoomChat>?>()
    val getRoomChatList: LiveData<List<RoomChat>?> get () = _getRoomChatList

    private val _getGalleryList = MutableLiveData<List<Uri>?>()
    val getGalleryList: LiveData<List<Uri>?> get() = _getGalleryList

    private val _loadingState = MutableLiveData<Boolean>(false)
    val loadingState: LiveData<Boolean> get() = _loadingState

    private val _itemSelectedPos = MutableLiveData<String>()
    val itemSelectedPos : LiveData<String> get() = _itemSelectedPos

    fun setItemSelected(uri: String){
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

    fun getRoomChatList(roomId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getRoomChat(roomId).collect() {
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
                            if(it.data?.isEmpty() == true){
                                _getRoomChatList.value = null
                            } else _getRoomChatList.value = it.data
                        }
                    }

                }
            }
        }
    }

    fun sendMessage(message: String, roomId : String) {
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

    fun sendImageMessage(message: String, roomId : String) {
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
                            if(it.data!!.isEmpty()){
                                _getGalleryList.value = null
                            } else _getGalleryList.value = it.data
                        }
                    }
                }
            }
        }
    }


}