package com.github.rednit.likes

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.github.rednit.R
import com.github.rednit.TinderConnection
import com.github.rednit.util.ImageUtil

class LikeAdapter(private val imageUtil: ImageUtil) :
    RecyclerView.Adapter<LikeAdapter.LikeViewHolder>() {

    private var photos = emptyList<String>()
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
    fun updateContent(photos: List<String>) {
        this.photos = photos
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {
        if (photos.isEmpty()) {
            return
        }

        val url = photos[position]
        val context = holder.imageView.context

        holder.imageView.setOnLongClickListener {
            it.showContextMenu()
            true
        }

        holder.imageView.setOnCreateContextMenuListener { menu, _, _ ->
            menu.add("Open in Browser").setOnMenuItemClickListener {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                true
            }

            menu.add("Share").setOnMenuItemClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                    .setType("image/*")
                    .putExtra(Intent.EXTRA_TEXT, "Look who liked me on Tinder!")

                imageUtil.asyncBitmap(url) {
                    intent.putExtra(Intent.EXTRA_STREAM, imageUtil.cache(it!!, context))
                    context.startActivity(intent)
                }
                true
            }
        }
        imageUtil.drawable(url).into(holder.imageView)
    }
}