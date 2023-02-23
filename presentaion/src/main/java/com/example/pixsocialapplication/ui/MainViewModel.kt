package com.example.pixsocialapplication.ui

import android.app.Application
import android.view.View
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.lifecycle.*
import com.example.domain.appdata_usecase.AppDataUseCase
import com.example.domain.core.Result
import com.example.domain.model.RoomInfo
import com.example.domain.usecase.UseCase
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.utils.DLog
import com.example.pixsocialapplication.utils.NetworkConnection
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application,
    private val useCase: UseCase,
    private val appDataUseCase: AppDataUseCase
    ) : ViewModel() {

    private val _appbarTitle = MutableSharedFlow<String>()
    val appbarTitle get() = _appbarTitle.asLiveData()

    private val _appbarDesc = MutableSharedFlow<String>()
    val appbarDesc get() = _appbarDesc.asLiveData()

    private val _fabVisible = MutableSharedFlow<Int>(View.VISIBLE)
    val fabVisible get() = _fabVisible.asLiveData()

    private val _bottomVisible = MutableSharedFlow<Int>()
    val bottomVisible get() = _bottomVisible.asLiveData()

    val network = NetworkConnection(application)

//    fun findChatUser(userId: String){
//        viewModelScope.launch(Dispatchers.IO) {
//            useCase.findUserId(userId).collect(){
//                when (it){
//                    is Result.Error -> {
//                        _bottomVisible.emit(BottomSheetBehavior.STATE_HIDDEN)
//                    }
//                    is Result.Loading -> {
//
//                    }
//                    is Result.Success -> {
//                        _bottomVisible.emit(BottomSheetBehavior.STATE_HIDDEN)
//                    }
//                }
//            }
//        }
//    }

    fun findChatUser(userId: String){
        viewModelScope.launch(Dispatchers.IO) {
            if (network.value == true) {
                appDataUseCase.addFriends(userId).collect(){
                    when (it){
                        is Result.Error -> {
                            DLog().d(it.exception.toString())
                            _bottomVisible.emit(BottomSheetBehavior.STATE_HIDDEN)
                        }
                        is Result.Loading -> {

                        }
                        is Result.Success -> {
                            _bottomVisible.emit(BottomSheetBehavior.STATE_HIDDEN)
                        }
                    }
                }
            } else {

            }
        }
    }

    fun setAppbarTitle(title : String, desc : String){
        viewModelScope.launch(Dispatchers.IO) {
            _appbarTitle.emit(title)
            _appbarDesc.emit(desc)
        }
    }

    fun navAppbarTitle(title : String){
        viewModelScope.launch(Dispatchers.IO) {
            _appbarTitle.emit(title)
        }
    }

    fun setBottomVisible(visible : Int){
        viewModelScope.launch(Dispatchers.IO){
            _bottomVisible.emit(visible)
        }
    }

    fun setToken(key: String, token: String){
        useCase.setStringPreferences(key, token)
    }

    fun getToken(key : String): String{
        return useCase.getStringPreferences(key)
    }

    fun updateUserFcmToken(token : String){
        useCase.updateUserFcmToken(token)
    }
}