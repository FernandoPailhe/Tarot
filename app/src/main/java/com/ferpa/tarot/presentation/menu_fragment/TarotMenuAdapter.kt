package com.ferpa.tarot.presentation.menu_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ferpa.tarot.databinding.TarotMenuItemBinding
import com.ferpa.tarot.domain.model.TarotMenuItem

class TarotMenuAdapter(
    private val onItemClicked: (TarotMenuItem) -> Unit
) : ListAdapter<TarotMenuItem, TarotMenuAdapter.TarotMenuItemViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TarotMenuItemViewHolder {
        return TarotMenuItemViewHolder(
            TarotMenuItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TarotMenuItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    inner class TarotMenuItemViewHolder(
        private var binding: TarotMenuItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind Tarot UI Layout Item with Tarot menu element
         */
        fun bind(tarotMenuItem: TarotMenuItem) {

            binding.apply {
                textTitle.text = tarotMenuItem.title
                textDescription.text = tarotMenuItem.description
                setTarotTablePreview(tarotMenuItem.numberOfCards)
            }
        }

        /**
         * Determine the number of cards and sets the visibility of certain views in the
         * tarotTablePreview object.
         */
        private fun setTarotTablePreview(numberOfCards: Int) {
            when (numberOfCards) {
                1 -> {
                    binding.tarotTablePreview.firstCard.layoutSpace.visibility = View.INVISIBLE
                    binding.tarotTablePreview.secondCard.layoutSpace.visibility = View.INVISIBLE
                    binding.tarotTablePreview.thirdCard.layoutSpace.visibility = View.INVISIBLE
                    binding.tarotTablePreview.fourthCard.layoutSpace.visibility = View.INVISIBLE
                }
                3 -> {
                    binding.tarotTablePreview.fourthCard.layoutSpace.visibility = View.INVISIBLE
                    binding.tarotTablePreview.thirdCard.layoutSpace.visibility = View.INVISIBLE
                }
            }
        }

    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<TarotMenuItem>() {
            override fun areItemsTheSame(oldItem: TarotMenuItem, newItem: TarotMenuItem): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: TarotMenuItem,
                newItem: TarotMenuItem
            ): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }

}