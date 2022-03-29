package com.github.rednit.likes

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.rednit.R
import com.github.rednit.TinderConnection

class LikeAdapter : RecyclerView.Adapter<LikeAdapter.LikeViewHolder>() {

    private var photos = emptyList<Bitmap>()
    private val connection = TinderConnection.connection

    class LikeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_view)
    }

    override fun getItemCount(): Int {
        return connection.likePreviewCount()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.like_view, parent, false)
        return LikeViewHolder(layout)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateContent(newPhotos: List<Bitmap>) {
        photos = newPhotos

        for (i in newPhotos.size..10) {
            notifyItemRemoved(i)
        }

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {
        if (photos.isEmpty()) {
            return
        }
        holder.imageView.setImageBitmap(photos[position])
    }
}