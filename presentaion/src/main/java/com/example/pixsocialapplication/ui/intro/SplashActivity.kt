package com.example.pixsocialapplication.ui.intro

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.ActivitySplashBinding
import com.example.pixsocialapplication.ui.MainActivity
import com.example.pixsocialapplication.utils.DLog
import com.example.pixsocialapplication.utils.repeatOnStarted
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    var start = false
    private val logInViewModel : LogInViewModel by viewModels()
    private lateinit var binding : ActivitySplashBinding
    private var userState: Boolean = false

    private var count = 1;
    companion object {
        const val REQ_PERMISSION_MAIN = 1001
    }

    private val permissionList = listOf<String>(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_MEDIA_IMAGES,
//        android.Manifest.permission.POST_NOTIFICATIONS
    )

    private val permissionList_s = listOf<String>(
        android.Manifest.permission.READ_MEDIA_IMAGES,
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val content: View = findViewById(android.R.id.content)
        logInViewModel.signInGoogleAutoLogIn()

        repeatOnStarted {
            logInViewModel.skipIntro.collect{
                start = it
            }
        }

        repeatOnStarted {
            logInViewModel.state.collect{
                userState = it.launchGoogleSignIn
            }

        }

        val deniedPermission = checkPermissions()

        if (deniedPermission.size > 0) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                ActivityCompat.requestPermissions(
                    this@SplashActivity, permissionList_s.toTypedArray(), REQ_PERMISSION_MAIN
                )
            } else
                ActivityCompat.requestPermissions(
                    this@SplashActivity, permissionList.toTypedArray(), REQ_PERMISSION_MAIN
                )
        } else {
            CoroutineScope(Dispatchers.Main).async {
                delay(500)
                goToMain()
            }
        }

//        val deniedPermission = checkPermissions()
//
//        if (deniedPermission.size > 0) {
//            ActivityCompat.requestPermissions(
//                this@SplashActivity, permissionList.toTypedArray(), REQ_PERMISSION_MAIN
//            )
//        } else {
//
//            CoroutineScope(Dispatchers.Main).async {
//                delay(500)
//                goToMain()
////                goMain.await()
//            }
//        }



//        content.viewTreeObserver.addOnPreDrawListener(
//            object : ViewTreeObserver.OnPreDrawListener {
//                override fun onPreDraw(): Boolean {
//                    return if (start) {
//                        content.viewTreeObserver.removeOnPreDrawListener(this)
//                        true
//                    } else
//                        false
//                }
//            }
//        )

//        val rootView: View = getWindow().getDecorView().findViewById(android.R.id.content)
//        Snackbar.make(rootView,"hi", Snackbar.LENGTH_LONG).show()
    }

//    val goMain =  CoroutineScope(Dispatchers.Main).async {
//        delay(500)
//
//
//    }

    fun goToMain(){
        if (userState) {
            startActivity(Intent(baseContext, MainActivity::class.java))
            finish()
        } else {
            startActivity(Intent(baseContext, LogInActivity::class.java))
            finish()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            // 메인 퍼미션 권한 요청 코드
            REQ_PERMISSION_MAIN -> {
                val deniedPermissionList = mutableListOf<String>()
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    deniedPermissionList.add(permissions[0])
                }
//                grantResults.forEachIndexed { index, i ->
//                    if (i == PackageManager.PERMISSION_DENIED) {
//                        // 거부된 권한
//                        deniedPermissionList.add(permissions[index])
//                    }
//                }
                if (deniedPermissionList.size > 0) {
                    // 거부된 권한 존재
                    makePermissionSnackBar()
                } else {
                    // 권한 모두 동의
                    CoroutineScope(Dispatchers.Main).async {
                        delay(500)
                        goToMain()
                    }
                }
            }
        }
    }

    private fun makePermissionSnackBar() {
        val snackBar = Snackbar.make(findViewById(R.id.main_layout), R.string.permission_request, Snackbar.LENGTH_INDEFINITE)
        if (count < 2) {
            snackBar.setAction("권한승인"){
                count += 1
                ActivityCompat.requestPermissions(
                    this@SplashActivity, permissionList.toTypedArray(), REQ_PERMISSION_MAIN
                )
            }
        } else {
            snackBar.setAction("확인"){
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


    private fun checkPermissions() : MutableList<String> {
        val deniedPermissionList = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            permissionList_s.forEach {
                if (ContextCompat.checkSelfPermission(this, it) ==
                    PackageManager.PERMISSION_DENIED) {
                    // 거부된 권한
                    deniedPermissionList.add(it)
                }
            }
        } else {
            permissionList.forEach {
                if (ContextCompat.checkSelfPermission(this, it) ==
                    PackageManager.PERMISSION_DENIED) {
                    // 거부된 권한
                    deniedPermissionList.add(it)
                }
            }
        }

        return deniedPermissionList
    }

    override fun onStart() {
        super.onStart()

    }
}