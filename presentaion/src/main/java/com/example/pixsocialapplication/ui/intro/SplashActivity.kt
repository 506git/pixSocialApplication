package com.example.pixsocialapplication.ui.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.ActivityLogInBinding
import com.example.pixsocialapplication.databinding.ActivitySplashBinding
import com.example.pixsocialapplication.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    var start = false
    private val logInViewModel : LogInViewModel by viewModels()
    private lateinit var binding : ActivitySplashBinding
    private var userState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val content: View = findViewById(android.R.id.content)
        logInViewModel.signInGoogleAutoLogIn()

        logInViewModel.skipIntro.observe(this){
            start = it
        }

        logInViewModel.state.observe(this){
            userState = it.launchGoogleSignIn
        }

        CoroutineScope(Dispatchers.Main).async {
            delay(500)
            if (userState) {
                startActivity(Intent(baseContext, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(baseContext, LogInActivity::class.java))
                finish()
            }
        }

        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (start) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else
                        false
                }
            }
        )
    }
}