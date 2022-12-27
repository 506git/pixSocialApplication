package com.example.pixsocialapplication.ui

import android.view.View
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.core.Result
import com.example.domain.model.RoomInfo
import com.example.domain.usecase.UseCase
import com.example.pixsocialapplication.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {

    private val _appbarTitle = MutableLiveData<String>()
    val appbarTitle: LiveData<String> get() = _appbarTitle

    private val _appbarDesc = MutableLiveData<String>()
    val appbarDesc: LiveData<String> get() = _appbarDesc

    private val _fabVisible = MutableLiveData<Int>(View.VISIBLE)
    val fabVisible : LiveData<Int> get() = _fabVisible

    private val _bottomVisible = MutableLiveData<Int>()
    val bottomVisible : LiveData<Int> get() = _bottomVisible

    fun findChatUser(userId: String){
        viewModelScope.launch(Dispatchers.IO) {

            useCase.findUserId(userId).collect(){
                when (it){
                    is Result.Error -> {
                        withContext(Dispatchers.Main) {
                            _bottomVisible.value = BottomSheetBehavior.STATE_HIDDEN
                        }
                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        withContext(Dispatchers.Main) {
                            _bottomVisible.value = BottomSheetBehavior.STATE_HIDDEN
                        }
                    }
                }
            }
        }
    }

    fun setAppbarTitle(title : String, desc : String){
        _appbarTitle.value = title
        _appbarDesc.value = desc
    }

    fun setFabVisible(visible : Int){
        _fabVisible.value = visible
    }

    fun setBottomVisible(visible : Int){
        _bottomVisible.value = visible
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