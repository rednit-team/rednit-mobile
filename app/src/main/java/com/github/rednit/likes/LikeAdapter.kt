package com.github.rednit.likes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.rednit.R
import com.github.rednit.TinderConnection
import com.rednit.tinder4j.api.entities.user.LikePreview

class LikeAdapter : RecyclerView.Adapter<LikeAdapter.LikeViewHolder>() {

    private var list: List<LikePreview> = TinderConnection.client.likePreviews.complete()

    class LikeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.like_view, parent, false)

        return LikeViewHolder(layout)
    }

    override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {
        val image: Bitmap
        try {
            val input = java.net.URL(
                list[position].photos[0].url
            ).openStream()
            image = BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        holder.imageView.setImageBitmap(image)
    }

}