package com.github.rednit.likes

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.github.rednit.R
import com.github.rednit.TinderConnection
import java.io.ByteArrayOutputStream

class LikeAdapter : RecyclerView.Adapter<LikeAdapter.LikeViewHolder>() {

    private var photos = emptyList<LikeFragment.Photo>()
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
    fun updateContent(newPhotos: List<LikeFragment.Photo>) {
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

        val photo = photos[position]
        val context = holder.imageView.context

        holder.imageView.setOnLongClickListener {
            it.showContextMenu()
            true
        }

        holder.imageView.setOnCreateContextMenuListener { menu, _, _ ->
            menu.add("Open in Browser").setOnMenuItemClickListener {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(photo.url))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                true
            }

            menu.add("Share").setOnMenuItemClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                    .setType("image/*")
                    .putExtra(Intent.EXTRA_TEXT, "Look who liked me on Tinder!")
                val bitmap = photo.image
                val bytes = ByteArrayOutputStream()

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

                val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, System.currentTimeMillis().toString(), null)

                val uri = Uri.parse(path)

                intent.putExtra(Intent.EXTRA_STREAM, uri)

                context.startActivity(intent)
                true
            }

        }

        holder.imageView.setImageBitmap(photo.image)
    }
}