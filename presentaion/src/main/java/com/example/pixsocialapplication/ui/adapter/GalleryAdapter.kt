/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.pixsocialapplication.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.LibraryDataSearchList
import com.example.pixsocialapplication.databinding.ItemGalleryBinding
import com.example.pixsocialapplication.databinding.ItemSampleBinding
import com.example.pixsocialapplication.ui.chat.list.testData.Article
import com.example.pixsocialapplication.utils.DLog
import com.example.pixsocialapplication.utils.ImageLoader


/**
 * Adapter for an [Article] [List].
 */
class GalleryAdapter : PagingDataAdapter<Uri, GalleryAdapter.ViewHolder>(
    ARTICLE_DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemGalleryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        DLog().d("test adapter : ")
        val testList = getItem(position)
        if (testList != null) {
            holder.bind(testList)
        }
    }

    companion object {
        private val ARTICLE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                DLog().d("test adapter : ")
                return oldItem.toString() == newItem.toString()
            }


            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean =
                oldItem == newItem
        }
    }

    inner class ViewHolder(private val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(test: Uri) {
            DLog().d("test :")
            DLog().d("test : $test")
            binding.apply {
                ImageLoader(context = binding.imgGallery.context).imageLoadWithURL(
                    test.toString(),
                    binding.imgGallery
                )
//                binding.textTitle.text = test.toString()

            }
        }
    }

}
