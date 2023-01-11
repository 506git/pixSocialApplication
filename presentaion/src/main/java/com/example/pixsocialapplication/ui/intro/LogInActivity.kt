package com.example.pixsocialapplication.ui.intro

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.ActivityLogInBinding
import com.example.pixsocialapplication.ui.MainActivity
import com.example.pixsocialapplication.utils.AuthResultContract
import com.example.pixsocialapplication.utils.CommonUtils
import com.example.ssolrangapplication.common.setSafeOnClickListener
import com.google.android.gms.common.api.ApiException

import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LogInActivity : AppCompatActivity() {

    private val logInViewModel : LogInViewModel by viewModels()
    private lateinit var binding : ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoogleSign.setSafeOnClickListener {
            googleSignInLauncher.launch(1)
//            logInViewModel.googleLogIn()
//            startActivity(Intent(baseContext, MainActivity::class.java))
        }

        logInViewModel.state.observe(this){
            if (it.launchGoogleSignIn && it.databaseInit) {
//                logInViewModel.updateUserProfile("test")
                startActivity(Intent(baseContext, MainActivity::class.java))
                finish()
            }
        }

        binding.imgTitle.setImageBitmap(CommonUtils.convertPixelArt(resources, R.drawable.pic_icon))
    }

    private val googleSignInLauncher = registerForActivityResult(AuthResultContract()) { task ->
        try {
            val account = task?.getResult(ApiException::class.java)
            if (account == null) {

            } else {
                logInViewModel.signInWithGoogleIdToken(account.idToken.toString())
            }
        } catch (e: ApiException) {

        }
    }
}