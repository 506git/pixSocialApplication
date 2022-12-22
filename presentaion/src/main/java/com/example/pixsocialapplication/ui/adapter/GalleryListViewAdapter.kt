package com.example.pixsocialapplication.ui.adapter

import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.RoomInfo
import com.example.pixsocialapplication.R
import com.example.pixsocialapplication.utils.ImageLoader

import timber.log.Timber

class GalleryListViewAdapter(dataSet: ArrayList<Uri>) : RecyclerView.Adapter<GalleryListViewAdapter.ViewHolder>() {
    private val eventList : ArrayList<Uri> = dataSet

    interface galleryItemClickListener{
        fun onItemClick(position: Int)
    }

    private lateinit var mItemClickListener: galleryItemClickListener

    fun setGalleryItemClickListener(itemClickListener : galleryItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false))

//    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
//        (holder.view as? TextView)?.also {
//            it.text = eventList[position]
//            it.clipToOutline = true
//            val backgroundColorResId = if (position % 2 == 0) R.color.black else R.color.purple_700
//            it.setBackgroundColor(ContextCompat.getColor(it.context, backgroundColorResId))
//        }
//    }
    fun addItem(dataSet: ArrayList<Uri>){
        eventList.clear()
        eventList.addAll(dataSet)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener{
                mItemClickListener.onItemClick(absoluteAdapterPosition)
            }
        }
        private val imgGallery = itemView.findViewById<ImageView>(R.id.img_gallery)
        fun bind(event: Uri) {
            ImageLoader(context = itemView.context).imageLoadWithURL(event.toString(), imgGallery)
        }
    }

    override fun onBindViewHolder(holder: GalleryListViewAdapter.ViewHolder, position: Int) {
        holder.bind(eventList[position])
    }

    override fun getItemCount(): Int = eventList.count()


//    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}