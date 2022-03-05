package com.mradzinski.probablelamp.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mradzinski.probablelamp.data.model.Character
import com.mradzinski.probablelamp.databinding.ItemCharacterBinding

class CharactersAdapter(
    private inline val onItemClicked: ((Character, Int) -> Unit)? = null
) :
    ListAdapter<Character, CharactersAdapter.CharactersViewHolder>(
        object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean = oldItem == newItem
        }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CharactersViewHolder(binding).apply {
            binding.root.setOnClickListener {
                onItemClicked?.invoke(getItem(this.bindingAdapterPosition), this.bindingAdapterPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(character = getItem(position))
    }

    inner class CharactersViewHolder(private val binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(character: Character) {
            binding.name.text = character.name

            binding.speciesAndType.text = if (character.type.isNotEmpty()) {
                "${character.species} | ${character.type}"
            } else {
                character.species
            }

            Glide.with(binding.avatar)
                .load(character.image)
                .circleCrop()
                .into(binding.avatar)
                .clearOnDetach()
        }
    }
}