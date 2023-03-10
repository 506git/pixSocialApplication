package com.example.pixsocialapplication.ui.gallery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pixsocialapplication.databinding.ActivityGalleryBinding
import com.example.pixsocialapplication.ui.adapter.GalleryAdapter
import com.example.pixsocialapplication.ui.chat.list.ChatViewModel
import com.example.pixsocialapplication.ui.common.CommonActivity
import com.example.pixsocialapplication.utils.CommonUtils
import com.example.pixsocialapplication.utils.MessageEvent
import com.example.pixsocialapplication.utils.flowLib.repeatOnStarted
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class GalleryActivity : CommonActivity() {

    private lateinit var binding: ActivityGalleryBinding

    private val galleryViewModel : ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val galleryAdapter = GalleryAdapter().apply {
            setGalleryItemClickListener(object : GalleryAdapter.galleryItemClickListener{
                override fun onItemClick(position: Int, uri : Uri) {
                    sendBroadcast(Intent("gallery").apply { putExtra("uri","$uri") })
                    finish()
                }
            })
        }

        binding.listGallery.apply {
            adapter = galleryAdapter
            layoutManager = GridLayoutManager(context, 3)
            setHasFixedSize(true)
        }

        repeatOnStarted {
            galleryViewModel.eventFlow.collect() { event ->
                handleEvent(event)
            }
        }

        repeatOnStarted {
            galleryViewModel.pagingData.collectLatest {
                galleryAdapter.submitData(it)
            }
        }
    }

    private fun handleEvent(event: MessageEvent) = when (event) {
        is MessageEvent.ShowToast -> CommonUtils.snackBar(this@GalleryActivity, event.text, Snackbar.LENGTH_SHORT)
        is MessageEvent.Loading -> if (event.visible) show() else hide()
        else -> { }
    }
}

//private fun ActivityGalleryBinding.bindAdapter(galleryAdapter: GalleryAdapter) {
//    listGallery.adapter = galleryAdapter
//    listGallery.layoutManager = GridLayoutManager(listGallery.context,3)
////    listGallery.layoutManager = LinearLayoutManager(listGallery.context)
////    val decoration = DividerItemDecoration(chatList.context, DividerItemDecoration.VERTICAL)
////    chatList.addItemDecoration(decoration)
//}