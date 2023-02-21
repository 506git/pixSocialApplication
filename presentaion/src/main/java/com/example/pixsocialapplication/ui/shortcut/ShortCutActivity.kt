package com.example.pixsocialapplication.ui.shortcut

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.pixsocialapplication.R

class ShortCutActivity : AppCompatActivity() {
    var shortcutManager: ShortcutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_short_cut)

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
            .setShortLabel("Naver")
            .setLongLabel("Open the Naver")
            .setIcon(Icon.createWithResource(this, R.drawable.pic_icon))
            .setIntent(
                Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.naver.com/"))
            )
            .build()

        shortcutManager?.dynamicShortcuts = listOf(shortcut)
    }
}