package com.example.pixsocialapplication.ui.common

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

open class CommonActivity : AppCompatActivity() {

    private val dialog by lazy {
        LoadingDialog(this@CommonActivity)
    }

    fun show(){
        dialog.show()
    }

    fun hide(){
        dialog.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        hide()
        super.onDestroy()
    }
}