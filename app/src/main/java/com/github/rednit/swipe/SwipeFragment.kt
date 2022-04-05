package com.github.rednit.swipe

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.github.rednit.TinderConnection
import com.github.rednit.databinding.FragmentSwipeBinding
import com.yuyakaido.android.cardstackview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.URL

class SwipeFragment : Fragment(), CardStackListener {

    private lateinit var binding: FragmentSwipeBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var cardStackView: CardStackView
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardStackAdapter
    private val connection = TinderConnection.connection

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSwipeBinding.inflate(inflater, container, false)
        drawerLayout = binding.drawerLayout
        cardStackView = binding.cardStackView
        manager = CardStackLayoutManager(requireContext(), this)
        binding.cardStackView.isVisible = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CardStackAdapter()
        setupCardStackView()

        lifecycleScope.launch {
            val cards = mutableListOf<CardStackAdapter.Card>()
            withContext(Dispatchers.IO) { cards.addAll(fetchUsers()) }
            adapter.updateCards(cards)
            binding.cardStackView.isVisible = true
            binding.progressBar.isVisible = false
        }
    }

    private suspend fun fetchUsers(): List<CardStackAdapter.Card> {
        val result = mutableListOf<CardStackAdapter.Card>()
        withContext(Dispatchers.IO) {
            connection.recommendations().forEach {
                result.add(
                    CardStackAdapter.Card(
                        it,
                        bitmap = BitmapFactory.decodeStream(
                            URL(it.photos[0].url).openStream()
                        )
                    )
                )
            }
        }
        return result
    }


    override fun onCardSwiped(direction: Direction) {
        if (manager.topPosition == adapter.getCards().size) {
            binding.cardStackView.isVisible = false
            binding.progressBar.isVisible = true

            lifecycleScope.launch {
                val cards = mutableListOf<CardStackAdapter.Card>()
                withContext(Dispatchers.IO) { cards.addAll(fetchUsers()) }
                adapter.updateCards(cards)
                binding.cardStackView.isVisible = true
                binding.progressBar.isVisible = false
            }
        }
    }

    private fun setupCardStackView() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun paginate() {
        runBlocking {
            val old = adapter.getCards()
            val new = old.plus(fetchUsers())
            val callback = CardDiffCallback(old, new)
            val result = DiffUtil.calculateDiff(callback)
            adapter.updateCards(new)
            result.dispatchUpdatesTo(adapter)
        }
    }

    override fun onCardAppeared(view: View, position: Int) {
    }

    override fun onCardDisappeared(view: View, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardRewound() {
    }

    override fun onCardCanceled() {
    }

}