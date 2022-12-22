package com.example.pixsocialapplication.ui.gallery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.ActivityGalleryBinding
import com.example.pixsocialapplication.databinding.FragmentGalleryBinding
import com.example.pixsocialapplication.ui.adapter.GalleryListViewAdapter
import com.example.pixsocialapplication.ui.chat.list.ChatViewModel
import com.example.pixsocialapplication.ui.common.LoadingDialog
import com.example.pixsocialapplication.utils.DLog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding

    private val galleryViewModel : ChatViewModel by viewModels()
    private lateinit var galleryListViewAdapter : GalleryListViewAdapter

    private var galleryArray = arrayListOf<Uri>()

    private val dialog by lazy {
        LoadingDialog(this@GalleryActivity)
    }

    private lateinit var receiver : BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        galleryViewModel.getImageList()

        galleryViewModel.getGalleryList.observe(this){
            if (it == null){
                galleryArray = arrayListOf()
            } else galleryArray = it as ArrayList<Uri>

            galleryListViewAdapter.addItem(galleryArray)
            galleryListViewAdapter.notifyDataSetChanged()
        }
        galleryListViewAdapter = GalleryListViewAdapter(galleryArray)
        galleryListViewAdapter.setGalleryItemClickListener(object : GalleryListViewAdapter.galleryItemClickListener{
            override fun onItemClick(position: Int) {

                sendBroadcast(Intent("gallery").apply { putExtra("uri","${galleryArray[position]}") })
                finish()
//
//                DLog().d("activity : ${galleryArray[position]}")
//                galleryViewModel.setItemSelected(galleryArray[position])
            }

        })

        binding.listGallery.apply {
            adapter = galleryListViewAdapter
            layoutManager = GridLayoutManager(context, 3)
            setHasFixedSize(true)
        }

        galleryViewModel.loadingState.observe(this){
            if (it) dialog.show()
            else if (!it) dialog.dismiss()
        }
    }

}