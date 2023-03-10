package com.example.pixsocialapplication.ui.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.appdata_usecase.AppDataUseCase
import com.example.domain.core.Result
import com.example.domain.database_usecase.DatabaseUseCase
import com.example.domain.model.UserInfoVO
import com.example.domain.usecase.UseCase
import com.example.pixsocialapplication.utils.CommonEvent
import com.example.pixsocialapplication.utils.Config
import com.example.pixsocialapplication.utils.flowLib.MutableEventFlow
import com.example.pixsocialapplication.utils.flowLib.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val useCase: UseCase,
    private val appDataUseCase: AppDataUseCase,
    private val databaseUseCase: DatabaseUseCase
) : ViewModel() {

    private val _eventFlow = MutableEventFlow<CommonEvent>()
    val eventFlow get() = _eventFlow.asEventFlow()

    fun signInGoogleAutoLogIn() {
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.googleAutoLogIn().collect() {
                when (it) {
                    is Result.Error -> {
                        event(CommonEvent.Loading(false))
                        event(CommonEvent.OffLine(false))
                        event(CommonEvent.ShowToast(it.exception.toString()))
                        event(CommonEvent.GoMain(false))
                    }
                    is Result.Loading -> {
                        event(CommonEvent.Loading(true))
                    }
                    is Result.Success -> {
                        event(CommonEvent.Loading(true))
                        setID(it.data!!)
                        initUserInfoUpdateLocalDB()
                        event(CommonEvent.GoMain(true))

                    }
                }
            }
        }
    }

    private suspend fun event(event: CommonEvent) {
        _eventFlow.emit(event)
    }

    fun signInWithGoogleIdToken(idToken: String) {
        viewModelScope.launch(IO) {
            appDataUseCase.signInWithGoogleIdToken(idToken).collect() {
                when (it) {
                    is Result.Error -> {
                        event(CommonEvent.Loading(false))
                        event(CommonEvent.OffLine(false))
                        event(CommonEvent.ShowToast(it.exception.toString()))
                    }
                    is Result.Loading -> {
                        event(CommonEvent.Loading(true))
                    }
                    is Result.Success -> {
                        event(CommonEvent.Loading(false))
                        setID(it.data!!)
                        initUserInfoUpdateLocalDB()
                        event(CommonEvent.GoMain(true))
                    }
                }
            }
        }
    }

    fun initUserInfoUpdateLocalDB() {
        viewModelScope.launch(IO) {
            databaseUseCase.insertUserInfo().collect() {
                when (it) {
                    is Result.Error -> {

                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {

                    }
                }
            }
        }
    }

    private fun setID(data: UserInfoVO) {
        useCase.setStringPreferences(Config._ID, data._id)
        setName(data.name)
        settingId()
    }

    private fun setName(userName : String){
        Config.userName = userName
    }

    private fun settingId(){
        Config.userId = useCase.getStringPreferences(Config._ID)
    }


}