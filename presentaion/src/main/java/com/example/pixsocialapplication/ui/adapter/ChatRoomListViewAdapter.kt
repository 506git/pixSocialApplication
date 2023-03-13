package com.example.pixsocialapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.vo.ChatListVO
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.databinding.ItemChatImgRevBinding
import com.example.pixsocialapplication.databinding.ItemChatImgSentBinding
import com.example.pixsocialapplication.databinding.ItemChatTxtNoticeBinding
import com.example.pixsocialapplication.databinding.ItemChatTxtRevBinding
import com.example.pixsocialapplication.databinding.ItemChatTxtSentBinding
import com.example.pixsocialapplication.ui.chat.viewholder.*
import com.example.pixsocialapplication.utils.DLog
import com.example.pixsocialapplication.utils.ImageLoader

class ChatRoomListViewAdapter(dataSet: ArrayList<ChatListVO>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val eventList: ArrayList<ChatListVO> = dataSet

    interface ChatItemClickListener {
        fun onItemClick(position: Int)
    }

    interface ChatItemLongClickListener {
        fun onItemLongClick(position: Int)
    }

    private lateinit var mItemClickListener: ChatItemClickListener
    private lateinit var mItemLongClickListener: ChatItemLongClickListener

    fun setChatItemClickListener(itemClickListener: ChatItemClickListener) {
        mItemClickListener = itemClickListener
    }

    fun setChatItemLongClickListener(itemLongClickListener: ChatItemLongClickListener){
        mItemLongClickListener = itemLongClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_TEXT_MESSAGE_RECEIVED -> {
                return TextMsgRcvViewHolder(
                    ItemChatTxtRevBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            VIEW_TYPE_TEXT_MESSAGE_SENT -> {
                return TextMsgSentViewHolder(
                    ItemChatTxtSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            VIEW_TYPE_IMAGE_MESSAGE_RECEIVED -> {
                return ImageMsgRcvViewHolder(
                    ItemChatImgRevBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            VIEW_TYPE_IMAGE_MESSAGE_SENT -> {
                return ImageMsgSentViewHolder(
                    ItemChatImgSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            VIEW_TYPE_NOTICE_MESSAGE -> {
                return NoticeMsgViewHolder(
                    ItemChatTxtNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            else -> {
                throw IllegalArgumentException("Invalid view type")
            }
        }
    }

    fun addAllItem(dataSet: ArrayList<ChatListVO>) {
        eventList.clear()
        eventList.addAll(dataSet)
    }

    fun addItem(dataSet: ChatListVO) {
//        eventList.clear()
        eventList.add(dataSet)
    }

    override fun getItemViewType(position: Int): Int {
        val message = eventList[position]
        return if (message.message_sender == "me") {
            if (message.message_type == "image"){
                VIEW_TYPE_IMAGE_MESSAGE_SENT
            } else {
                VIEW_TYPE_TEXT_MESSAGE_SENT
            }
        } else if (message.message_sender == "you"){
            if (message.message_type == "image"){
                VIEW_TYPE_IMAGE_MESSAGE_RECEIVED
            } else {
                VIEW_TYPE_TEXT_MESSAGE_RECEIVED
            }
        } else VIEW_TYPE_NOTICE_MESSAGE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            VIEW_TYPE_TEXT_MESSAGE_SENT -> {
                val holder = holder as TextMsgSentViewHolder
                holder.bind(eventList[position], mItemClickListener, mItemLongClickListener)
            }
            VIEW_TYPE_IMAGE_MESSAGE_SENT -> {
                val holder = holder as ImageMsgSentViewHolder
                holder.bind(eventList[position], mItemClickListener, mItemLongClickListener)
            }
            VIEW_TYPE_TEXT_MESSAGE_RECEIVED -> {
                val holder = holder as TextMsgRcvViewHolder
                holder.bind(eventList[position], mItemClickListener, mItemLongClickListener)
            }
            VIEW_TYPE_IMAGE_MESSAGE_RECEIVED -> {
                val holder = holder as ImageMsgRcvViewHolder
                holder.bind(eventList[position], mItemClickListener, mItemLongClickListener)
            }
            VIEW_TYPE_NOTICE_MESSAGE -> {
                val holder = holder as NoticeMsgViewHolder
                holder.bind(eventList[position])
            }
        }
    }

    override fun getItemCount(): Int = eventList.count()

    companion object {
        const val VIEW_TYPE_TEXT_MESSAGE_RECEIVED = 1
        const val VIEW_TYPE_TEXT_MESSAGE_SENT = 2
        const val VIEW_TYPE_IMAGE_MESSAGE_RECEIVED = 3
        const val VIEW_TYPE_IMAGE_MESSAGE_SENT = 4
        const val VIEW_TYPE_NOTICE_MESSAGE = 5
    }
}