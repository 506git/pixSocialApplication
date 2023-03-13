package com.example.pixsocialapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.RoomInfo
import com.example.domain.model.RoomListInfo
import com.example.domain.vo.RoomListInfoVO
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.utils.ImageLoader

class UserRoomListViewAdapter(dataSet: ArrayList<RoomListInfoVO>) : RecyclerView.Adapter<UserRoomListViewAdapter.ViewHolder>() {
    private val eventList : ArrayList<RoomListInfoVO> = dataSet

    interface RoomItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    private lateinit var mItemClickListener: RoomItemClickListener

    fun setRoomItemClickListener(itemClickListener : RoomItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_room_list, parent, false))

//    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
//        (holder.view as? TextView)?.also {
//            it.text = eventList[position]
//            it.clipToOutline = true
//            val backgroundColorResId = if (position % 2 == 0) R.color.black else R.color.purple_700
//            it.setBackgroundColor(ContextCompat.getColor(it.context, backgroundColorResId))
//        }
//    }
    fun addItem(dataSet: ArrayList<RoomListInfoVO>){
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
        fun bind(event: RoomListInfoVO) {
            txtName.text = event.roomName
            txtId.text = event.id
            imgRoom.setOnClickListener {
                mItemClickListener.onItemClick(it, absoluteAdapterPosition)
            }
            ImageLoader(context = itemView.context).imageCircleLoadWithURL(event.roomImage.toString(), imgRoom)
        }
    }

    override fun onBindViewHolder(holder: UserRoomListViewAdapter.ViewHolder, position: Int) {
        holder.bind(eventList[position])
    }

    override fun getItemCount(): Int = eventList.count()


//    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}