package com.example.pixsocialapplication.ui.settings.main

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.database_usecase.DatabaseUseCase
import com.example.domain.usecase.UseCase
import com.example.pixsocialapplication.utils.DLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.domain.core.Result
import com.example.domain.model.User
import com.example.pixsocialapplication.utils.CommonUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val useCase: UseCase, private val databaseUseCase: DatabaseUseCase) : ViewModel() {
    private val _buildVersion = MutableLiveData<String>()
    val buildVersion : LiveData<String> get() = _buildVersion

    private val _userInfo = MutableLiveData<User>()
    val userInfo : LiveData<User> get() = _userInfo

//    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
//    val uiState: StateFlow<UiState> = _uiState

    private val _state = MutableStateFlow<Result<User>>(Result.Loading())
    val state : StateFlow<com.example.domain.core.Result<User>> = _state

    fun getBuildVersion(context: Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            _buildVersion.value = context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0)).versionName.toString()
        else _buildVersion.value = context.packageManager.getPackageInfo(context.packageName,0).versionName.toString()
    }

    fun deleteCache(context: Context){
        try {
            context.cacheDir.deleteRecursively()
        } catch (e: Exception) {
            DLog.d(e.toString())
        }
    }

    fun getUserInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            databaseUseCase.getUserInfo().collect() {
                when(it){
                    is Result.Error -> {

                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        withContext(Dispatchers.Main) {
                            _userInfo.value = it.data!!
                        }
                    }
                }
            }
        }
    }

    fun logout(){
        useCase.signOut()
    }

    init {
        viewModelScope.launch {
            databaseUseCase.getUserInfo().collect(){
                when(it){
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        _state.value = it
                    }
                    is Result.Error -> {

                    }
                }
            }
        }
    }
}