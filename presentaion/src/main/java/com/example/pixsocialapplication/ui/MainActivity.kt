package com.example.pixsocialapplication.ui

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.icu.number.Scale.none
import android.net.Uri
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
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.ActivityMainBinding
import com.example.pixsocialapplication.ui.settings.SettingsActivity
import com.example.pixsocialapplication.utils.CommonUtils
import com.example.pixsocialapplication.utils.DLog
import com.example.pixsocialapplication.utils.setSafeOnClickListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var behavior: BottomSheetBehavior<LinearLayout>
    private val mainViewModel: MainViewModel by viewModels()
    private var time: Long = 0
    var shortcutManager: ShortcutManager? = null

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(System.currentTimeMillis() - time >= 2000) {
                if (behavior.state == BottomSheetBehavior.STATE_EXPANDED){
                    mainViewModel.setBottomVisible(BottomSheetBehavior.STATE_HIDDEN)
                } else if (this@MainActivity.findNavController(R.id.nav_main_fragment).currentDestination?.id == R.id.chatRoomFragment){
                    time = System.currentTimeMillis();
                    CommonUtils.snackBar(this@MainActivity, "한번더 누르면 종료됩니다.", Snackbar.LENGTH_SHORT)
                } else {
                    this@MainActivity.findNavController(R.id.nav_main_fragment).popBackStack()
                }
            } else if(System.currentTimeMillis() - time < 2000 ){
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

        // fcm 구독
        Firebase.messaging.subscribeToTopic("pix_all").addOnCompleteListener { task ->
            var msg = "Subscribed"
            if (!task.isSuccessful) {
                msg = "Subscribe failed"
            }
        }

        // fcm 토큰
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                DLog().e(task.exception.toString())
                return@addOnCompleteListener
            }
            mainViewModel.setToken(getString(R.string.fcm_user_token), task.result)
            mainViewModel.updateUserFcmToken(task.result)
        }

        val navView : BottomNavigationView = binding.bottomNavigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_main_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id){
                R.id.chatListFragment -> {
                    binding.bottomNavigation.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    mainViewModel.navAppbarTitle(title = destination.label.toString())
                }
            }
        }

        navView.setOnItemSelectedListener { item ->
            when(item.itemId){
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

        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_add_chat, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)

        behavior = BottomSheetBehavior.from(binding.bottomView.bottomSheet)
            .apply {
                this.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {

                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {

                    }
                })
            }

        mainViewModel.appbarTitle.observe(this) {
            binding.appbarTxt.text = it.toString()
        }

        mainViewModel.appbarDesc.observe(this) {
            binding.appbarDesc.text = it.toString()
        }

        mainViewModel.bottomVisible.observe(this) {
            behavior.state = it
        }

        binding.bottomView.editSearch.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                mainViewModel.findChatUser(binding.bottomView.editSearch.text.toString().trim())
                mainViewModel.setBottomVisible(BottomSheetBehavior.STATE_COLLAPSED)
            }
            return@setOnKeyListener false
        }

        binding.btnSettings.setSafeOnClickListener {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
        }

        behavior.apply {
            isDraggable = true
            state = BottomSheetBehavior.STATE_HIDDEN
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
            .setIntent( Intent(applicationContext, SettingsActivity::class.java).apply { action = Intent.ACTION_VIEW })
            .setIcon(Icon.createWithResource(this, R.drawable.pic_icon))
            .build()

        shortcutManager?.dynamicShortcuts = listOf(shortcut)
    }
}