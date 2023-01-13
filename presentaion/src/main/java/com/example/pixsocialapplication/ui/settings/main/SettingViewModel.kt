package com.example.pixsocialapplication.ui.settings.main

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.usecase.UseCase
import com.example.pixsocialapplication.utils.DLog
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val useCase: UseCase) : ViewModel() {
    private val _buildVersion = MutableLiveData<String>()
    val buildVersion : LiveData<String> get() = _buildVersion

    fun getBuildVersion(context: Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            _buildVersion.value = context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0)).versionName.toString()
        else _buildVersion.value = context.packageManager.getPackageInfo(context.packageName,0).versionName.toString()
    }

    fun deleteCache(context: Context){
        try {
            context.cacheDir.deleteRecursively()
        } catch (e: Exception) {
            DLog().d(e.toString())
        }
    }

    fun logout(){

    }

}