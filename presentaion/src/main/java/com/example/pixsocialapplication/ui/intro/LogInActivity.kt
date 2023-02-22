package com.example.pixsocialapplication.ui.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.ActivityLogInBinding
import com.example.pixsocialapplication.ui.MainActivity
import com.example.pixsocialapplication.utils.*
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInActivity : AppCompatActivity() {

    private val logInViewModel : LogInViewModel by viewModels()
    private lateinit var binding : ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.btnGoogleSign.setSafeOnClickListener {
//            googleSignInLauncher.launch(1)
//        }

        repeatOnStarted {
            logInViewModel.state.collect {
                if (it.launchGoogleSignIn && it.databaseInit) {
                    startActivity(Intent(baseContext, MainActivity::class.java))
                    finish()
                }
            }
        }

        repeatOnStarted {
            logInViewModel.eventFlow.collect { event -> handleEvent(event, this@LogInActivity) }
        }

        with(binding){
            imgTitle.setImageBitmap(CommonUtils.convertPixelArt(resources, R.drawable.pic_icon))
            btnGoogleSign.setSafeOnClickListener { googleSignInLauncher.launch(1) }
        }
//
//        binding.imgTitle.setImageBitmap(CommonUtils.convertPixelArt(resources, R.drawable.pic_icon))
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