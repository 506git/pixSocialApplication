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
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.utils.ImageLoader

import timber.log.Timber

class ChatRoomListViewAdapter(dataSet: ArrayList<RoomChat>) :
    RecyclerView.Adapter<ChatRoomListViewAdapter.ViewHolder>() {
    private val eventList: ArrayList<RoomChat> = dataSet

    interface ChatItemClickListener {
        fun onItemClick(position: Int)
    }

    private lateinit var mItemClickListener: ChatItemClickListener

    fun setChatItemClickListener(itemClickListener: ChatItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == VIEW_TYPE_ME)
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_room_chat_me, parent, false)
            )
        else
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_room_chat_you, parent, false)
            )
    }


    //    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
//        (holder.view as? TextView)?.also {
//            it.text = eventList[position]
//            it.clipToOutline = true
//            val backgroundColorResId = if (position % 2 == 0) R.color.black else R.color.purple_700
//            it.setBackgroundColor(ContextCompat.getColor(it.context, backgroundColorResId))
//        }
//    }
    fun addItem(dataSet: ArrayList<RoomChat>) {
        eventList.clear()
        eventList.addAll(dataSet)
    }

    override fun getItemViewType(position: Int): Int {
        return if (eventList[position].messageSender == "me") VIEW_TYPE_ME else VIEW_TYPE_YOU
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                mItemClickListener.onItemClick(absoluteAdapterPosition)
            }
        }

        private val txtMessage = itemView.findViewById<TextView>(R.id.text_message)
        private val txtName = itemView.findViewById<TextView>(R.id.text_name)
        private val imgRoom = itemView.findViewById<ImageView>(R.id.img_room)
        private val imgMessage = itemView.findViewById<ImageView>(R.id.img_message)
        fun bind(event: RoomChat) {
            if (event.messageType == "photo"){
                txtMessage.visibility = View.GONE
                imgMessage.visibility = View.VISIBLE
                ImageLoader(context = itemView.context).imageLoadWithURL(
                    event.message.toString(),
                    imgMessage
                )
            } else {
                txtMessage.visibility = View.VISIBLE
                txtMessage.text = event.message?.trim()
                imgMessage.visibility = View.GONE
            }
            txtName.text = event.messageSenderName
            ImageLoader(context = itemView.context).imageCircleLoadWithURL(
                event.messageSenderImage.toString(),
                imgRoom
            )
        }
    }

    override fun onBindViewHolder(holder: ChatRoomListViewAdapter.ViewHolder, position: Int) {
        holder.bind(eventList[position])
    }

    override fun getItemCount(): Int = eventList.count()

    companion object {
        private const val VIEW_TYPE_ME = 1
        private const val VIEW_TYPE_YOU = 2
    }

//    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}