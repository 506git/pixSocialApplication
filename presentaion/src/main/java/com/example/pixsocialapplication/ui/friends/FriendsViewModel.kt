package com.example.pixsocialapplication.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.domain.appdata_usecase.AppDataUseCase
import com.example.domain.core.Result
import com.example.domain.model.FriendInfo
import com.example.domain.usecase.UseCase
import com.example.pixsocialapplication.utils.Config
import com.example.pixsocialapplication.utils.DLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val appDataUseCase: AppDataUseCase,
    private val useCase: UseCase,
) : ViewModel() {

    private val _loadingState = MutableSharedFlow<Boolean>()
    val loadingState get() = _loadingState.asSharedFlow()

    private val _getRoomList = MutableSharedFlow<List<FriendInfo>?>()
    val getRoomList get() = _getRoomList.asSharedFlow()

//    fun getRoomListRepos() {
//        viewModelScope.launch(Dispatchers.IO) {
//            useCase.getRoomInfo().collect() {
//                when (it) {
//                    is Result.Error -> {
//                        _loadingState.emit(false)
//                    }
//                    is Result.Loading -> {
//                        _loadingState.emit(true)
//                    }
//                    is Result.Success -> {
//                        _loadingState.emit(false)
//
//                        if (it.data!!.isEmpty()) {
//                            _getRoomList.emit(null)
//                        } else _getRoomList.emit(it.data)
//                    }
//                }
//            }
//        }
//    }

    fun getFriendsListRepos() {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.getFriends(getID(Config._ID)).collect() {
                when (it) {
                    is Result.Error -> {
                        _loadingState.emit(false)
                        DLog().d(it.exception.toString())
                    }
                    is Result.Loading -> {
                        _loadingState.emit(true)
                    }
                    is Result.Success -> {
                        _loadingState.emit(false)
                        DLog().d(it.data.toString())

                        if (it.data?.result?.content?.isEmpty() == true) {
                            _getRoomList.emit(null)
                        } else _getRoomList.emit(it.data?.result?.content)

                    }
                }
            }
        }
    }

    fun getID(key : String): String{
        return useCase.getStringPreferences(key)
    }

}