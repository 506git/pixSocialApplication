package com.example.pixsocialapplication.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pixsocialapplication.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_right_exit, R.anim.slide_left_enter)
    }
}