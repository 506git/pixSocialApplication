package com.example.pixsocialapplication.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.ActivitySettingsBinding
import com.example.pixsocialapplication.ui.common.CommonActivity
import com.example.pixsocialapplication.utils.CommonUtils
import com.example.pixsocialapplication.utils.setSafeOnClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsActivity : CommonActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(this@SettingsActivity.supportFragmentManager.findFragmentById(R.id.nav_main_fragment)?.childFragmentManager?.backStackEntryCount == 0){
                finish()
            }else this@SettingsActivity.findNavController(R.id.nav_main_fragment).popBackStack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            btnBack.setSafeOnClickListener {
                onBackPressed()
            }
        }

        // 뒤로가기
        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_right_exit, R.anim.slide_left_enter)
    }
}