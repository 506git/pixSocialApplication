package com.example.pixsocialapplication.ui.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.DialogLoadingBinding
import com.example.pixsocialapplication.utils.DLog

class LoadingDialog constructor(context: Context) : Dialog(context) {

    lateinit var binding : DialogLoadingBinding
    init {
        setCanceledOnTouchOutside(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogLoadingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.animationView.addValueCallback(KeyPath("Panel1", "White Solid 1"), LottieProperty.COLOR_FILTER){
            SimpleColorFilter(Color.TRANSPARENT)
        }
    }
}