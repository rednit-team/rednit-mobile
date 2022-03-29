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
import com.github.rednit.R
import com.github.rednit.TinderConnection
import com.github.rednit.databinding.FragmentLikeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


class LikeFragment : Fragment() {

    private lateinit var binding: FragmentLikeBinding
    private lateinit var recyclerView: RecyclerView

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
            val likePreviews = withContext(Dispatchers.IO) { connection.likePreviews() }
            val photos = mutableListOf<Bitmap>()
            withContext(Dispatchers.IO) {
                likePreviews.reversed().stream().map { entry ->
                    entry.photos[0].processedFiles.stream().filter {
                        it.height == 800
                    }.findFirst().get()
                }.forEach {
                    photos.add(BitmapFactory.decodeStream(URL(it.url).openStream()))
                }
            }

            adapter.updateContent(photos)

            val teaserCount = withContext(Dispatchers.IO) { connection.teaserCount() }

            if (teaserCount < 10) {
                binding.textLikeCount.isVisible = false
            } else {
                binding.textLikeCount.text = getString(R.string.like_count_text).format(teaserCount)
            }
        }
    }
}