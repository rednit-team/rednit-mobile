package com.github.rednit.swipe

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.rednit.R
import com.github.rednit.util.ImageUtil
import com.rednit.tinder4j.api.entities.user.swipeable.Recommendation

class CardStackAdapter(val imageUtil: ImageUtil) :
    RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    private var cards = listOf<Recommendation>()

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
    fun updateContent(cards: List<Recommendation>) {
        this.cards = cards
        notifyDataSetChanged()
    }

    fun getCards(): List<Recommendation> {
        return cards
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (cards.isEmpty() || cards.size <= position) {
            return
        }

        val user = cards[position]
        holder.name.text = user.name
        holder.age.text = user.age.toString()
        imageUtil.drawable(user.photos[0].url).into(holder.image)
    }

    override fun getItemCount(): Int {
        return cards.size
    }
}
