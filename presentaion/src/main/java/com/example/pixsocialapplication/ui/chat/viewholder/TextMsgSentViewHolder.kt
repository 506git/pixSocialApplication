package com.example.pixsocialapplication.ui.chat.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.domain.vo.ChatListVO
import com.example.pixsocialapplication.databinding.ItemChatTxtSentBinding
import com.example.pixsocialapplication.ui.adapter.ChatRoomListViewAdapter

class TextMsgSentViewHolder(val binding: ItemChatTxtSentBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        chat: ChatListVO,
        mItemClickListener: ChatRoomListViewAdapter.ChatItemClickListener,
        mItemLongClickListener: ChatRoomListViewAdapter.ChatItemLongClickListener
    ) {
        with(binding){
            textMessage.text = chat.message_body.trim()
            textMessage.setOnLongClickListener {
                mItemLongClickListener.onItemLongClick(absoluteAdapterPosition)
                return@setOnLongClickListener false
            }

            textDate.text = chat.createdAt
        }
    }
}