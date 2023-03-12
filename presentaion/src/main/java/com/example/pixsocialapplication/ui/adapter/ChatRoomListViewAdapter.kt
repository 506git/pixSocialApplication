package com.example.pixsocialapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.RoomChat
import com.example.domain.model.RoomInfo
import com.example.domain.vo.ChatListVO
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.utils.CommonUtils
import com.example.pixsocialapplication.utils.DLog
import com.example.pixsocialapplication.utils.ImageLoader

import timber.log.Timber

class ChatRoomListViewAdapter(dataSet: ArrayList<ChatListVO>) : RecyclerView.Adapter<ChatRoomListViewAdapter.ViewHolder>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType){
            VIEW_TYPE_TEXT_MESSAGE_RECEIVED -> {
                return ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_room_chat_you, parent, false)
                )
            }
            VIEW_TYPE_TEXT_MESSAGE_SENT -> {
                return ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_room_chat_me, parent, false)
                )
            }
            VIEW_TYPE_IMAGE_MESSAGE_RECEIVED -> {
                return ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_room_chat_you, parent, false)
                )
            }
            VIEW_TYPE_IMAGE_MESSAGE_SENT -> {
                return ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_room_chat_me, parent, false)
                )
            }
            VIEW_TYPE_NOTICE_MESSAGE -> {
                return ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_room_chat_you, parent, false)
                )
            }
            else -> {
                throw IllegalArgumentException("Invalid view type")
            }
        }
    }


    //    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
//        (holder.view as? TextView)?.also {
//            it.text = eventList[position]
//            it.clipToOutline = true
//            val backgroundColorResId = if (position % 2 == 0) R.color.black else R.color.purple_700
//            it.setBackgroundColor(ContextCompat.getColor(it.context, backgroundColorResId))
//        }
//    }
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
                VIEW_TYPE_TEXT_MESSAGE_SENT
            } else {
                VIEW_TYPE_IMAGE_MESSAGE_SENT
            }
        } else {
            if (message.message_type == "image"){
                VIEW_TYPE_TEXT_MESSAGE_RECEIVED
            } else {
                VIEW_TYPE_IMAGE_MESSAGE_RECEIVED
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init { }

        private val txtMessage = itemView.findViewById<TextView>(R.id.text_message)
        private val txtName = itemView.findViewById<TextView>(R.id.text_name)
        private val imgRoom = itemView.findViewById<ImageView>(R.id.img_room)
        private val imgMessage = itemView.findViewById<ImageView>(R.id.img_message)
        private val txtDate = itemView.findViewById<TextView>(R.id.text_date)
        fun bind(event: ChatListVO) {
            if (event.message_type == "image"){
                txtMessage.visibility = View.GONE
                imgMessage.visibility = View.VISIBLE
                ImageLoader(context = itemView.context).imageLoadWithURL(
                    event.message_body.toString(),
                    imgMessage
                )
                imgMessage.setOnClickListener {
                    mItemClickListener.onItemClick(absoluteAdapterPosition)
                }
                imgMessage.setOnLongClickListener {
                    mItemLongClickListener.onItemLongClick(absoluteAdapterPosition)
                    return@setOnLongClickListener false
                }
            } else {
                txtMessage.visibility = View.VISIBLE
                txtMessage.text = event.message_body?.trim()
                imgMessage.visibility = View.GONE
                txtMessage.setOnLongClickListener {
                    mItemLongClickListener.onItemLongClick(absoluteAdapterPosition)
                    return@setOnLongClickListener false
                }
            }
            if(event.message_sender != "me") {
                txtName.text = event.message_name
                ImageLoader(context = itemView.context).imageCircleLoadWithURL(
                    event.message_profile,
                    imgRoom
                )
            }
            txtDate.text = event.createdAt

        }
    }

    override fun onBindViewHolder(holder: ChatRoomListViewAdapter.ViewHolder, position: Int) {
        holder.bind(eventList[position])
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