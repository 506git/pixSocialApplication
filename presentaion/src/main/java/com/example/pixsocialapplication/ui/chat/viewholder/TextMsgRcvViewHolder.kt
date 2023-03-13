package com.example.pixsocialapplication.ui.chat.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.domain.vo.ChatListVO
import com.example.pixsocialapplication.databinding.ItemChatTxtRevBinding
import com.example.pixsocialapplication.ui.adapter.ChatRoomListViewAdapter
import com.example.pixsocialapplication.utils.DLog
import com.example.pixsocialapplication.utils.ImageLoader

class TextMsgRcvViewHolder(val binding: ItemChatTxtRevBinding) : RecyclerView.ViewHolder(binding.root) {
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

            textName.text = chat.message_name
            ImageLoader(context = itemView.context).imageCircleLoadWithURL(
                chat.message_profile,
                imgRoom
            )
            textDate.text = chat.createdAt
        }
    }
}