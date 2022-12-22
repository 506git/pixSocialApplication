package com.example.pixsocialapplication.ui.chat.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.ActivityImageDatailBinding
import com.example.pixsocialapplication.utils.ImageLoader

class ImageDatailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityImageDatailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDatailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = intent.getStringExtra("imageUri")
        val imageName = intent.getStringExtra("name")
        binding.appbarTxt.setText(imageName)
        ImageLoader(this@ImageDatailActivity).imageLoadWithURL(imageUri.toString(),binding.imgDetail)
    }
}