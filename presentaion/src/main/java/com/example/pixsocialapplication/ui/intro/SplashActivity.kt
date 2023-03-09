package com.example.pixsocialapplication.ui.intro

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.ActivitySplashBinding
import com.example.pixsocialapplication.ui.MainActivity
import com.example.pixsocialapplication.utils.CommonUtils
import com.example.pixsocialapplication.utils.Config
import com.example.pixsocialapplication.utils.repeatOnStarted
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val logInViewModel: LogInViewModel by viewModels()
    private lateinit var binding: ActivitySplashBinding

    private var count = 1;

    private val permissionList = listOf<String>(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        android.Manifest.permission.READ_MEDIA_IMAGES,
    )

    private val permissionList_s = listOf<String>(
        android.Manifest.permission.READ_MEDIA_IMAGES,
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val deniedPermission = checkPermissions()

        if (deniedPermission.size > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this@SplashActivity, permissionList_s.toTypedArray(), Config.REQ_PERMISSION_MAIN
                )
            } else
                ActivityCompat.requestPermissions(
                    this@SplashActivity, permissionList.toTypedArray(), Config.REQ_PERMISSION_MAIN
                )
        } else logInViewModel.signInGoogleAutoLogIn()

        repeatOnStarted {
            delay(500)
            logInViewModel.eventFlow.collect { event ->
                handleEvent(event)
            }
        }
    }

    private fun goToMain(state: Boolean) {
        if (state) startActivity(Intent(baseContext, MainActivity::class.java))
        else startActivity(Intent(baseContext, LogInActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            // 메인 퍼미션 권한 요청 코드
            Config.REQ_PERMISSION_MAIN -> {
                if (grantResults.isNotEmpty()) {
                    val deniedPermissionList = mutableListOf<String>()
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        deniedPermissionList.add(permissions[0])
                    }

                    if (deniedPermissionList.size > 0) {
                        // 거부된 권한 존재
                        makePermissionSnackBar()
                    } else {
                        // 권한 모두 동의
                        CoroutineScope(Dispatchers.Main).async {
                            delay(500)
                            goToMain(false)
                        }
                    }
                }
            }
        }
    }

    private fun makePermissionSnackBar() {
        val snackBar = Snackbar.make(
            findViewById(R.id.main_layout),
            R.string.permission_request,
            Snackbar.LENGTH_INDEFINITE
        )
        if (count < 2) {
            snackBar.setAction("권한승인") {
                count += 1
                ActivityCompat.requestPermissions(
                    this@SplashActivity, permissionList.toTypedArray(), Config.REQ_PERMISSION_MAIN
                )
            }
        } else {
            snackBar.setAction("확인") {
                count = count++
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
        snackBar.apply {
            setBackgroundTint(ContextCompat.getColor(this@SplashActivity, R.color.main_color))
            setTextColor(ContextCompat.getColor(this@SplashActivity, R.color.white))
            setActionTextColor(ContextCompat.getColor(this@SplashActivity, R.color.white))
        }.show()

    }

    private fun checkPermissions(): MutableList<String> {
        val deniedPermissionList = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionList_s.forEach {
                if (ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED) {
                    // 거부된 권한
                    deniedPermissionList.add(it)
                }
            }
        } else {
            permissionList.forEach {
                if (ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED) {
                    // 거부된 권한
                    deniedPermissionList.add(it)
                }
            }
        }
        return deniedPermissionList
    }

    private fun handleEvent(event: LogInViewModel.Event) = when (event) {
        is LogInViewModel.Event.ShowToast -> CommonUtils.snackBar(
            this,
            event.text,
            Snackbar.LENGTH_SHORT
        )
        is LogInViewModel.Event.OffLine -> CommonUtils.networkState = event.state
        is LogInViewModel.Event.GoMain -> goToMain(event.state)

    }
}