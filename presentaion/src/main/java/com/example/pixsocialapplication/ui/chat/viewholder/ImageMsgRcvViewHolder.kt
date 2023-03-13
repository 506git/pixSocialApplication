package com.example.pixsocialapplication.ui.chat.viewholder

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.vo.ChatListVO
import com.example.pixsocialapplication.databinding.ItemChatImgRevBinding
import com.example.pixsocialapplication.ui.adapter.ChatRoomListViewAdapter
import com.example.pixsocialapplication.utils.DLog
import com.example.pixsocialapplication.utils.ImageLoader

class ImageMsgRcvViewHolder(val binding: ItemChatImgRevBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        chat: ChatListVO,
        mItemClickListener: ChatRoomListViewAdapter.ChatItemClickListener,
        mItemLongClickListener: ChatRoomListViewAdapter.ChatItemLongClickListener
    ) {
        with(binding){
            ImageLoader(context = binding.root.context).imageCircleLoadWithURL(
                chat.message_profile,
                imgRoom
            )
            ImageLoader(context = binding.root.context).imageLoadWithURL(
                chat.message_body,
                imgMessage
            )
            imgMessage.setOnClickListener {
                mItemClickListener.onItemClick(absoluteAdapterPosition)
            }
            imgMessage.setOnLongClickListener {
                mItemLongClickListener.onItemLongClick(absoluteAdapterPosition)
                return@setOnLongClickListener false
            }

            textName.text = chat.message_name

            textDate.text = chat.createdAt
        }
    }
}