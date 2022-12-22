package com.example.pixsocialapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.ActivityMainBinding
import com.example.pixsocialapplication.ui.chat.room.ChatRoomViewModel
import com.example.pixsocialapplication.utils.DLog
import com.example.ssolrangapplication.common.setSafeOnClickListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var behavior: BottomSheetBehavior<LinearLayout>
    private val mainViewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAddChat.setSafeOnClickListener {
//            behavior.state =
            mainViewModel.setBottomVisible(BottomSheetBehavior.STATE_EXPANDED)
        }
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_add_chat, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)

        behavior = BottomSheetBehavior.from(binding.bottomView.bottomSheet)
            .apply {
            this.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }
            })
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful){
                DLog().e(task.exception.toString())
                return@addOnCompleteListener
            }
            mainViewModel.setToken(getString(R.string.fcm_user_token), task.result)
            mainViewModel.updateUserFcmToken(task.result)
        }


        mainViewModel.fabVisible.observe(this){
            binding.fabAddChat.visibility = it
        }

        mainViewModel.appbarTitle.observe(this){
            binding.appbarTxt.text = it.toString()
        }

        mainViewModel.appbarDesc.observe(this){
            binding.appbarDesc.text = it.toString()
        }

        mainViewModel.bottomVisible.observe(this){
            behavior.state = it
        }

        binding.bottomView.editSearch.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                mainViewModel.findChatUser(binding.bottomView.editSearch.text.toString().trim())
//                behavior.state = BottomSheetBehavior.STATE_HIDDEN
                mainViewModel.setBottomVisible(BottomSheetBehavior.STATE_COLLAPSED)
            }
            return@setOnKeyListener false
        }

        behavior.apply {
//            isGestureInsetBottomIgnored = true
            isDraggable = true
            state = BottomSheetBehavior.STATE_HIDDEN
        }
    }
}