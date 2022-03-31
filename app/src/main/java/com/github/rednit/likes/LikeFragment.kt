package com.github.rednit.likes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rednit.TinderConnection
import com.github.rednit.databinding.FragmentLikeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


class LikeFragment : Fragment() {

    private lateinit var binding: FragmentLikeBinding
    private lateinit var recyclerView: RecyclerView
    private var photos = mutableListOf<Photo>()

    data class Photo(val url: String, val image: Bitmap)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLikeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        val adapter = LikeAdapter()
        recyclerView.adapter = adapter

        val connection = TinderConnection.connection

        lifecycleScope.launch {
            if (photos.isEmpty()) withContext(Dispatchers.IO) {
                val likePreviews = withContext(Dispatchers.IO) { connection.likePreviews() }
                likePreviews.stream().forEach {
                    val url = it.photos[0].url
                    val sizedImage = it.photos[0].processedFiles.stream().filter { sizedImage ->
                        sizedImage.height == 800
                    }.findFirst().get()
                    photos.add(
                        Photo(
                            url,
                            BitmapFactory.decodeStream(URL(sizedImage.url).openStream())
                        )
                    )
                }
            }

            adapter.updateContent(photos)

            if (photos.isEmpty()) {
                binding.textNoLikes.isVisible = true
            }

            binding.progressBar.isVisible = false
        }
    }
}