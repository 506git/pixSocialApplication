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
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.FriendInfo
import com.example.domain.model.LibraryDataSearchList
import com.example.domain.model.RoomInfo
import com.example.domain.vo.FriendsInfoVO
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.ItemSampleBinding
import com.example.pixsocialapplication.ui.chat.list.testData.Article
import com.example.pixsocialapplication.utils.ImageLoader


/**
 * Adapter for an [Article] [List].
 */
class FriendsAdapter(dataSet: ArrayList<FriendsInfoVO>) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {
    private val eventList : ArrayList<FriendsInfoVO> = dataSet

    interface FriendItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    private lateinit var mItemClickListener: FriendItemClickListener

    fun setFriendItemClickListener(itemClickListener : FriendItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_room_list, parent, false))

    fun addItem(dataSet: ArrayList<FriendsInfoVO>){
        eventList.clear()
        eventList.addAll(dataSet)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener{
                mItemClickListener.onItemClick(it, absoluteAdapterPosition)
            }
        }
        private val txtName = itemView.findViewById<TextView>(R.id.text_name)
        private val txtId = itemView.findViewById<TextView>(R.id.text_id)
        private val imgRoom = itemView.findViewById<ImageView>(R.id.img_room)
        fun bind(event: FriendsInfoVO) {
            txtName.text = event.name
            txtId.text = event.comment
            imgRoom.setOnClickListener {
                mItemClickListener.onItemClick(it, absoluteAdapterPosition)
            }
            ImageLoader(context = itemView.context).imageCircleLoadWithURL(event.picture.toString(), imgRoom)
        }
    }

    override fun onBindViewHolder(holder: FriendsAdapter.ViewHolder, position: Int) {
        holder.bind(eventList[position])
    }

    override fun getItemCount(): Int = eventList.count()

}