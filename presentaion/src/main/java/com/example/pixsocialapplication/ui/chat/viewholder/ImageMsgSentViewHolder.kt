package com.example.pixsocialapplication.ui.chat.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.domain.vo.ChatListVO
import com.example.pixsocialapplication.databinding.ItemChatImgSentBinding
import com.example.pixsocialapplication.ui.adapter.ChatRoomListViewAdapter
import com.example.pixsocialapplication.utils.ImageLoader

class ImageMsgSentViewHolder(val binding: ItemChatImgSentBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        chat: ChatListVO,
        mItemClickListener: ChatRoomListViewAdapter.ChatItemClickListener,
        mItemLongClickListener: ChatRoomListViewAdapter.ChatItemLongClickListener
    ) {
        with(binding){
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

            textDate.text = chat.createdAt
        }
    }
}