package com.example.pixsocialapplication.ui.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.utils.DLog

class LoadingDialog constructor(context: Context) : Dialog(context) {
    init {
        setCanceledOnTouchOutside(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_loading)
    }
}