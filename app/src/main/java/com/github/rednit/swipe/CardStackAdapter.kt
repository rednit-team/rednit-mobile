package com.github.rednit.swipe

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.rednit.R
import com.rednit.tinder4j.api.entities.user.swipeable.Recommendation

class CardStackAdapter : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    data class Card(
        val user: Recommendation,
        val bitmap: Bitmap
    )

    private var cards = listOf<Card>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.text_name)
        var age: TextView = view.findViewById(R.id.text_age)
        var image: ImageView = view.findViewById(R.id.swipe_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.swipe_card, parent, false))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCards(cards: List<Card>) {
        this.cards = cards
        notifyDataSetChanged()
    }

    fun getCards(): List<Card> {
        return cards
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (cards.isEmpty() || cards.size <= position) {
            return
        }

        val spot = cards[position]
        holder.name.text = spot.user.name
        holder.age.text = spot.user.age.toString()
        holder.image.setImageBitmap(spot.bitmap)
    }

    override fun getItemCount(): Int {
        return cards.size
    }
}
