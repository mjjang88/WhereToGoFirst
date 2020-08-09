package com.mjjang.wheretogofirst.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mjjang.wheretogofirst.data.Place
import com.mjjang.wheretogofirst.databinding.ListItemPlaceBinding

class PlaceListAdapter() : ListAdapter<Place, RecyclerView.ViewHolder>(PlaceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlaceViewHolder(
            ListItemPlaceBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val place = getItem(position)
        (holder as PlaceViewHolder).bind(place)

        holder.binding.setClickListener {
            mPlaceListClickListener?.onItemClick(it, place)
        }

        holder.binding.setViewLocateBtnClickListener {
            mPlaceListClickListener?.OnViewLocateButtonClick(it, place)
        }
    }

    class PlaceViewHolder(
        val binding: ListItemPlaceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Place) {
            binding.apply {
                place = item
                executePendingBindings()
            }
        }
    }

    var mPlaceListClickListener : OnPlaceListClickListener? = null
    fun setPlaceListClickListener(listener: OnPlaceListClickListener) {
        mPlaceListClickListener = listener
    }
}

interface OnPlaceListClickListener {
    fun onItemClick(view: View, place: Place)
    fun OnViewLocateButtonClick(view: View, place: Place)
}

private class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {

    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }
}