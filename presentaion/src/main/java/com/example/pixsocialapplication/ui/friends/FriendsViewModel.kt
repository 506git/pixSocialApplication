package com.example.pixsocialapplication.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.appdata_usecase.AppDataUseCase
import com.example.domain.core.Result
import com.example.domain.model.RoomInfo
import com.example.domain.usecase.UseCase
import com.example.pixsocialapplication.utils.DLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val appDataUseCase: AppDataUseCase,
    private val useCase: UseCase,
) : ViewModel() {

    private val _loadingState = MutableSharedFlow<Boolean>()
    val loadingState get() = _loadingState.asSharedFlow()

    private val _getRoomList = MutableSharedFlow<List<RoomInfo>?>()
    val getRoomList get() = _getRoomList.asSharedFlow()

    fun getRoomListRepos() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getRoomInfo().collect() {
                when (it) {
                    is Result.Error -> {
                        _loadingState.emit(false)
                    }
                    is Result.Loading -> {
                        _loadingState.emit(true)
                    }
                    is Result.Success -> {
                        _loadingState.emit(false)

                        if (it.data!!.isEmpty()) {
                            _getRoomList.emit(null)
                        } else _getRoomList.emit(it.data)

                    }
                }
            }
        }
    }

    fun getRoomListRepos2() {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.getFriends("63f7723ecb364cff960958f7").collect() {
                when (it) {
                    is Result.Error -> {
                        _loadingState.emit(false)
                    }
                    is Result.Loading -> {
                        _loadingState.emit(true)
                    }
                    is Result.Success -> {
                        _loadingState.emit(false)
                        DLog().d(it.data.toString())

                    }
                }
            }
        }
    }

}