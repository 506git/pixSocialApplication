package com.example.pixsocialapplication.ui.chat.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.domain.vo.ChatListVO
import com.example.pixsocialapplication.databinding.ItemChatTxtNoticeBinding

class NoticeMsgViewHolder(val binding: ItemChatTxtNoticeBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(chat: ChatListVO) {
        with(binding){
            textMessage.text = chat.message_body
        }
    }
}