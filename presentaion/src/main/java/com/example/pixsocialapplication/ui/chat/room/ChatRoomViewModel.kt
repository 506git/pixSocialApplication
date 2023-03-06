package com.example.pixsocialapplication.ui.chat.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.appdata_usecase.AppDataUseCase
import com.example.domain.core.Result
import com.example.domain.model.LibraryDataSearchList
import com.example.domain.model.RoomInfo
import com.example.domain.model.RoomListInfo
import com.example.domain.model.Test
import com.example.domain.usecase.UseCase
import com.example.pixsocialapplication.ui.chat.list.testData.Article
import com.example.pixsocialapplication.utils.Config
import com.example.pixsocialapplication.utils.DLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject

private const val ITEMS_PER_PAGE = 50

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val useCase: UseCase,
    private val appDataUseCase: AppDataUseCase
) : ViewModel() {

    private val _getRoomList = MutableLiveData<List<RoomListInfo>?>()
    val getRoomList: LiveData<List<RoomListInfo>?> get() = _getRoomList

    private val _loadingState = MutableLiveData<Boolean>(false)
    val loadingState: LiveData<Boolean> get() = _loadingState

//    fun getRoomListRepos() {
//        viewModelScope.launch(Dispatchers.IO) {
//            useCase.getRoomInfo().collect() {
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
//                            if(it.data!!.isEmpty()){
//                                _getRoomList.value = null
//                            } else _getRoomList.value = it.data
//                        }
//                    }
//                }
//            }
//        }
//    }

    fun getRoomListRepos() {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.getRoomList(getID()).collect() {
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
                            DLog().d(it.data.toString())
                            _getRoomList.value = it.data?.result?.content ?: null
                        }
                    }
                }
            }
        }
    }

    fun getID(): String {
        return useCase.getStringPreferences(Config._ID)
    }

/*
    val pagingData: Flow<PagingData<LibraryDataSearchList>> =
        useCase.getTestData().cachedIn(viewModelScope)
    val pagingData: Flow<PagingData<Test>> = Pager(
       config =  PagingConfig(pageSize =  ITEMS_PER_PAGE, enablePlaceholders = false),
       pagingSourceFactory = {useCase.getTestData()}
    ).flow.cachedIn(viewModelScope)  paging */

}