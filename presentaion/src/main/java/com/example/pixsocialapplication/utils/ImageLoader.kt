package com.example.pixsocialapplication.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File

class ImageLoader(private val context: Context) {

    fun imageLoadWithResourceId(resId: Int, v : ImageView){
        Glide.with(this.context).load(resId).into(v)
    }

    fun imageLoadWithURL(url: String, v: ImageView){
        Glide.with(this.context).load(url).into(v)
    }
    fun imageCircleLoadWithURL(url: String, v: ImageView){
        Glide.with(this.context).load(url).circleCrop().into(v)
    }

    fun imageLoadWithFilePath(filePath: String, v: ImageView){
        Glide.with(this.context).load(filePath).into(v)
    }

    fun imageLoadWithFIle(f: File, v: ImageView){
        Glide.with(this.context).load(f).into(v)
    }

    fun imageLoadWithBitmap(b: Bitmap, v: ImageView){
        Glide.with(this.context).load(b).into(v)
    }
}