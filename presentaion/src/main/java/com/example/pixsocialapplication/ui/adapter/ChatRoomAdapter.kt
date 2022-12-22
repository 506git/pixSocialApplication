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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.LibraryDataSearchList
import com.example.pixsocialapplication.databinding.ItemSampleBinding
import com.example.pixsocialapplication.ui.chat.list.testData.Article
import com.example.pixsocialapplication.utils.ImageLoader


/**
 * Adapter for an [Article] [List].
 */
class ChatRoomAdapter : PagingDataAdapter<LibraryDataSearchList, ChatRoomAdapter.ViewHolder>(
    ARTICLE_DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemSampleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val testList = getItem(position)
        if (testList != null) {
            holder.bind(testList)
        }
    }

    companion object {
        private val ARTICLE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<LibraryDataSearchList>() {
            override fun areItemsTheSame(oldItem: LibraryDataSearchList, newItem: LibraryDataSearchList): Boolean =
                oldItem.bookKey == newItem.bookKey

            override fun areContentsTheSame(oldItem: LibraryDataSearchList, newItem: LibraryDataSearchList): Boolean =
                oldItem == newItem
        }
    }

    inner class ViewHolder(private val binding: ItemSampleBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(test: LibraryDataSearchList) {
            binding.apply {
                binding.textTitle.text = test.bookTitle
                binding.textLibCode.text = test.libraryCode
                binding.textLibName.text = test.libraryName
                ImageLoader(context = binding.imgBook.context).imageLoadWithURL(test.bookThumbnailURL,binding.imgBook)
            }
        }
    }

}
