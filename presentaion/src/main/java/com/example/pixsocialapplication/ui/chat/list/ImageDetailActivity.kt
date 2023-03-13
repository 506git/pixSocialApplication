package com.example.pixsocialapplication.ui.chat.list

import android.os.Bundle
import android.view.View
import com.example.pixsocialapplication.databinding.ActivityImageDatailBinding
import com.example.pixsocialapplication.model.ImageDetailModel
import com.example.pixsocialapplication.ui.common.CommonActivity
import com.example.pixsocialapplication.utils.DLog
import com.example.pixsocialapplication.utils.DateUtils
import com.example.pixsocialapplication.utils.ImageLoader
import com.example.pixsocialapplication.utils.setSafeOnClickListener

class ImageDetailActivity : CommonActivity() {

    private lateinit var binding : ActivityImageDatailBinding
    private var visible : Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDatailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageInfo = intent.getParcelableExtra("imageInfo", ImageDetailModel::class.java)

        with(binding){
            appbarTitle.text = imageInfo?.userName
            appbarCreateAt.text = imageInfo?.createdAt.toString()
            ImageLoader(this@ImageDetailActivity).imageLoadWithURL(imageInfo?.imageUrl.toString(), imgDetail)

            imgDetail.setOnClickListener {
                if (visible) {
                    appbarView.visibility = View.GONE
                    visible = false
                }
                else {
                    appbarView.visibility = View.VISIBLE
                    visible = true
                }
            }
        }
    }
}