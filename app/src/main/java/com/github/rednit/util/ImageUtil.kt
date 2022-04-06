package com.github.rednit.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.rednit.R
import java.io.File
import java.io.FileOutputStream
import java.util.function.Consumer

class ImageUtil(private val fragment: Fragment) {

    fun drawable(url: String): RequestBuilder<Drawable> {
        val progressBar = CircularProgressDrawable(fragment.requireContext())
        progressBar.strokeWidth = 5f
        progressBar.centerRadius = 30f
        progressBar.setColorSchemeColors(
            fragment.requireContext().getColor(R.color.secondaryVariant)
        )
        progressBar.start()
        return Glide.with(fragment)
            .load(url)
            .placeholder(progressBar)
            .error(R.drawable.image_error)
    }

    fun bitmap(url: String): RequestBuilder<Bitmap> {
        return Glide.with(fragment).asBitmap().load(url)
    }

    fun asyncBitmap(url: String, consumer: Consumer<Bitmap?>) {
        bitmap(url).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                consumer.accept(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                consumer.accept(null)
            }
        })
    }

    fun cache(bitmap: Bitmap, context: Context): Uri {
        val cache = File(context.cacheDir, "/images/")
        cache.mkdirs()
        val file = File(cache, System.currentTimeMillis().toString() + ".jpeg")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.close()
        return FileProvider.getUriForFile(context, "com.github.rednit.fileprovider", file)
    }

}
