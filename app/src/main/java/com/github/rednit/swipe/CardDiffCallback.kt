package com.github.rednit.swipe

import androidx.recyclerview.widget.DiffUtil

class CardDiffCallback(
    private val old: List<CardStackAdapter.Card>,
    private val new: List<CardStackAdapter.Card>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return old[oldPosition].user.id == new[newPosition].user.id
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return old[oldPosition] == new[newPosition]
    }

}