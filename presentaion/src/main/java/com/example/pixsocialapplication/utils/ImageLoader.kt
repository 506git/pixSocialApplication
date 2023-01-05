package com.example.pixsocialapplication.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.pixsocialapplication.R
import java.io.File


class ImageLoader(private val context: Context) {
    private val options = RequestOptions().placeholder(R.drawable.progress_animation).error(R.drawable.pic_icon).diskCacheStrategy(
        DiskCacheStrategy.AUTOMATIC).priority(Priority.HIGH)

    fun imageLoadWithResourceId(resId: Int, v : ImageView){
        Glide.with(this.context).load(resId).apply(options).into(v)
    }

    fun imageLoadWithURL(url: String, v: ImageView){
        Glide.with(this.context).load(url).apply(options).into(v)
    }
    fun imageCircleLoadWithURL(url: String, v: ImageView){
        Glide.with(this.context).load(url).circleCrop().apply(options).into(v)
    }

    fun imageLoadWithFilePath(filePath: String, v: ImageView){
        Glide.with(this.context).load(filePath).apply(options).into(v)
    }

    fun imageLoadWithFIle(f: File, v: ImageView){
        Glide.with(this.context).load(f).apply(options).into(v)
    }

    fun imageLoadWithBitmap(b: Bitmap, v: ImageView){
        Glide.with(this.context).load(b).apply(options).into(v)
    }
}