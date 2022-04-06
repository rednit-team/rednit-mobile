package com.github.rednit.likes

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
import com.github.rednit.util.ImageUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LikeFragment : Fragment() {

    private lateinit var binding: FragmentLikeBinding
    private lateinit var recyclerView: RecyclerView
    private var photos = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLikeBinding.inflate(inflater, container, false)
        binding.refreshLayout.setColorSchemeResources(R.color.primary)
        binding.refreshLayout.setProgressBackgroundColorSchemeColor(requireContext().getColor(R.color.text))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        val adapter = LikeAdapter(ImageUtil(this))
        recyclerView.adapter = adapter

        val connection = TinderConnection.connection

        lifecycleScope.launch {
            if (photos.isEmpty()) withContext(Dispatchers.IO) {
                val likePreviews = connection.likePreviews()
                likePreviews.stream().forEach { photos.add(it.photos[0].url) }
            }

            adapter.updateContent(photos)

            if (photos.isEmpty()) {
                binding.textNoLikes.isVisible = true
            }

            binding.progressBar.isVisible = false
            binding.refreshLayout.isRefreshing = false
        }

        binding.refreshLayout.setOnRefreshListener {
            photos.clear()
            connection.resetCache()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.container, LikeFragment())
                commit()
            }
        }
    }
}
