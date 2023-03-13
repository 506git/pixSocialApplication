package com.example.pixsocialapplication.ui.settings.main

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.core.Result
import com.example.domain.database_usecase.DatabaseUseCase
import com.example.domain.model.User
import com.example.domain.usecase.UseCase
import com.example.pixsocialapplication.utils.DLog
import com.example.pixsocialapplication.utils.MessageEvent
import com.example.pixsocialapplication.utils.SettingsEvent
import com.example.pixsocialapplication.utils.flowLib.MutableEventFlow
import com.example.pixsocialapplication.utils.flowLib.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val useCase: UseCase, private val databaseUseCase: DatabaseUseCase) : ViewModel() {

    private val _userInfo = MutableSharedFlow<User>()
    val userInfo get() = _userInfo.asSharedFlow()

    private val _state = MutableStateFlow<Result<User>>(Result.Loading())
    val state : StateFlow<Result<User>> = _state

    private val _eventFlow = MutableEventFlow<SettingsEvent>()
    val eventFlow get() = _eventFlow.asEventFlow()

    private suspend fun event(event: SettingsEvent) {
        _eventFlow.emit(event)
    }

    fun getBuildVersion(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            event(
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    SettingsEvent.Version(context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0)).versionName.toString())
                else SettingsEvent.Version(context.packageManager.getPackageInfo(context.packageName,0).versionName.toString())
            )
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
                        _userInfo.emit(it.data!!)
                    }
                }
            }
        }
    }

    fun logout() = useCase.signOut()
}