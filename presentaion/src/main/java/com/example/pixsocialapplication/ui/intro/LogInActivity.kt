package com.example.pixsocialapplication.ui.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.pixsocialapplication.databinding.ActivityLogInBinding
import com.example.pixsocialapplication.ui.MainActivity
import com.example.pixsocialapplication.ui.common.CommonActivity
import com.example.pixsocialapplication.utils.CommonEvent
import com.example.pixsocialapplication.utils.google.AuthResultContract
import com.example.pixsocialapplication.utils.CommonUtils
import com.example.pixsocialapplication.utils.flowLib.repeatOnStarted
import com.example.pixsocialapplication.utils.setSafeOnClickListener
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInActivity : CommonActivity() {

    private val logInViewModel : LogInViewModel by viewModels()
    private lateinit var binding : ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repeatOnStarted {
            logInViewModel.eventFlow.collect { event -> handleEvent(event) }
        }

        with(binding){
            btnGoogleSign.setSafeOnClickListener { googleSignInLauncher.launch(1) }
        }
    }

    private val googleSignInLauncher = registerForActivityResult(AuthResultContract()) { task ->
        try {
            val account = task?.getResult(ApiException::class.java)
            if (account != null) {
                logInViewModel.signInWithGoogleIdToken(account.idToken.toString())
            }
        } catch (e: ApiException) {

        }
    }

    private fun handleEvent(event: CommonEvent) = when (event) {
        is CommonEvent.ShowToast -> CommonUtils.snackBar(this, event.text, Snackbar.LENGTH_SHORT)
        is CommonEvent.OffLine -> CommonUtils.networkState = event.state
        is CommonEvent.GoMain -> startActivity(Intent(baseContext, MainActivity::class.java)).apply { finish() }
        is CommonEvent.Loading -> if (event.visible) show() else hide()
        else -> {}
    }
}