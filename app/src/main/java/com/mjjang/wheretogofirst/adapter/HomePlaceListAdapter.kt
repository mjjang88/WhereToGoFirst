package com.mjjang.wheretogofirst.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mjjang.wheretogofirst.data.DBProcedure
import com.mjjang.wheretogofirst.data.Place
import com.mjjang.wheretogofirst.databinding.ListItemHomePlaceBinding

class HomePlaceListAdapter() : ListAdapter<Place, RecyclerView.ViewHolder>(HomePlaceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HomePlaceViewHolder(
            ListItemHomePlaceBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val place = getItem(position)
        (holder as HomePlaceViewHolder).bind(place)
    }

    class HomePlaceViewHolder(
        private val binding: ListItemHomePlaceBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Place) {
            binding.apply {
                place = item
                executePendingBindings()
            }

            binding.setCloseBtnClickListner {
                DBProcedure.deletePlace(item)
            }
        }
    }
}

private class HomePlaceDiffCallback : DiffUtil.ItemCallback<Place>() {

    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }
}