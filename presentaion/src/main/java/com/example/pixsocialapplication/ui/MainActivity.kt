package com.example.pixsocialapplication.ui

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.ActivityMainBinding
import com.example.pixsocialapplication.ui.chat.room.ChatRoomViewModel
import com.example.pixsocialapplication.ui.common.CommonActivity
import com.example.pixsocialapplication.ui.settings.SettingsActivity
import com.example.pixsocialapplication.utils.*
import com.example.pixsocialapplication.utils.flowLib.repeatOnStarted
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : CommonActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var behavior: BottomSheetBehavior<LinearLayout>
    private val mainViewModel: MainViewModel by viewModels()
    private var time: Long = 0
    var shortcutManager: ShortcutManager? = null

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - time >= 2000) {
                if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    mainViewModel.setBottomVisible(BottomSheetBehavior.STATE_HIDDEN)
                } else if (this@MainActivity.findNavController(R.id.nav_main_fragment).currentDestination?.id == R.id.chatRoomFragment) {
                    time = System.currentTimeMillis();
                    CommonUtils.snackBar(this@MainActivity, "한번더 누르면 종료됩니다.", Snackbar.LENGTH_SHORT)
                } else {
                    this@MainActivity.findNavController(R.id.nav_main_fragment).popBackStack()
                }
            } else if (System.currentTimeMillis() - time < 2000) {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 뒤로가기
        this.onBackPressedDispatcher.addCallback(this, callback)

        // fcm 토큰
        if (mainViewModel.getToken(getString(R.string.fcm_user_token)).isEmpty()) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    DLog.e(task.exception.toString())
                    return@addOnCompleteListener
                }
                mainViewModel.setToken(getString(R.string.fcm_user_token), task.result)
                mainViewModel.updateUserFcmToken(task.result)
            }
        }

        with(binding) {
            bottomView.editSearch.setOnKeyListener { view, keyCode, keyEvent ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                    mainViewModel.findChatUser(binding.bottomView.editSearch.text.toString().trim())
                    mainViewModel.setBottomVisible(BottomSheetBehavior.STATE_COLLAPSED)
                }
                return@setOnKeyListener false
            }
            btnSettings.setSafeOnClickListener {
                startActivity(
                    Intent(this@MainActivity, SettingsActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
                overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
            }

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_main_fragment) as NavHostFragment

            val navController = navHostFragment.navController.apply {
                addOnDestinationChangedListener { controller, destination, arguments ->
                    when (destination.id) {
                        R.id.chatListFragment -> {
                            binding.bottomNavigation.visibility = View.GONE
                        }
                        else -> {
                            binding.bottomNavigation.visibility = View.VISIBLE
                            mainViewModel.navAppbarTitle(title = destination.label.toString())
                        }
                    }
                }
            }

            bottomNavigation.setupWithNavController(navController)

            bottomNavigation.apply {
                setOnItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.addFriendsFragment -> {
                            mainViewModel.setBottomVisible(BottomSheetBehavior.STATE_EXPANDED)
                            return@setOnItemSelectedListener false
                        }
                        else -> {
                            navController.navigate(item.itemId)
                            return@setOnItemSelectedListener true
                        }
                    }
                }
            }

            behavior = BottomSheetBehavior.from(bottomView.bottomSheet).apply {
                this.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {

                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {

                    }
                })
                isDraggable = true
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        repeatOnStarted {
            mainViewModel.eventFlow.collect{ event ->
                handleEvent(event)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                shortcutManager = getSystemService<ShortcutManager>(ShortcutManager::class.java)
                addNaverShortcut()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun addNaverShortcut() {
        val shortcut = ShortcutInfo.Builder(this, "shortcut_open_naver")
            .setShortLabel("Settings")
            .setLongLabel("Open the Settings")
            .setIntent(Intent(applicationContext, SettingsActivity::class.java).apply {
                action = Intent.ACTION_VIEW
            })
            .setIcon(Icon.createWithResource(this, R.drawable.pic_icon))
            .build()
        shortcutManager?.dynamicShortcuts = listOf(shortcut)
    }

    private fun handleEvent(event: MainEvent) = when (event) {
        is MainEvent.ShowToast -> CommonUtils.snackBar(this, event.text, Snackbar.LENGTH_SHORT)
        is MainEvent.OffLine -> CommonUtils.networkState = event.state
        is MainEvent.Loading -> if (event.visible) show() else hide()
        is MainEvent.AppbarTitle -> binding.appbarTxt.text = event.title
        is MainEvent.BottomBehavior -> behavior.state = event.state
        else -> {}
    }
}