package com.ferpa.tarot.presentation.result_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ferpa.tarot.databinding.CardDetailItemBinding
import com.ferpa.tarot.domain.model.Card
import com.ferpa.tarot.domain.model.bind

class ResultAdapter: ListAdapter<Card, ResultAdapter.CardViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResultAdapter.CardViewHolder {
        return CardViewHolder(
            CardDetailItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ResultAdapter.CardViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    inner class CardViewHolder(
        private var binding: CardDetailItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind Result UI Layout Item with Result card
         */
        fun bind(card: Card) {
            binding.apply {
                card.bind(imageCard, textCardTitle, textCardDescription)
            }
        }

    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Card>() {
            override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: Card,
                newItem: Card
            ): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }

}