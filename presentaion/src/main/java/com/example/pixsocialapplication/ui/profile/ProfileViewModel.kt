package com.example.pixsocialapplication.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.appdata_usecase.AppDataUseCase
import com.example.domain.core.Result
import com.example.domain.usecase.UseCase
import com.example.pixsocialapplication.utils.Config
import com.example.pixsocialapplication.utils.DLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val appDataUseCase: AppDataUseCase,
    private val useCase: UseCase
) : ViewModel() {

    fun chatRoomStart(_id : String) {
        val members = listOf<String>(getID(Config._ID), _id)
        viewModelScope.launch(Dispatchers.IO) {
            appDataUseCase.chatRoomStart(members).collect() {
                when (it) {
                    is Result.Error -> {
                        DLog.d(it.exception.toString())
                    }
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        DLog.d(it.data.toString())
                    }
                }
            }
        }
    }


    private fun getID(key: String): String {
        return useCase.getStringPreferences(key)
    }
}