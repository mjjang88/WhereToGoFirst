package com.mjjang.wheretogofirst.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mjjang.wheretogofirst.data.Place
import com.mjjang.wheretogofirst.databinding.ListItemPlaceBinding
import com.mjjang.wheretogofirst.manager.MyApplication

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

        holder.binding.setViewLocateBtnClickListener {
            mViewLocateBtnClickListener?.OnButtonClick(it, place)
        }
    }

    class PlaceViewHolder(
        val binding: ListItemPlaceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setClickListener {
                Toast.makeText(MyApplication.applicationContext(), "항목 선택됨. 이름 : " + binding.place?.name + ", 주소 : " + binding.place?.address, Toast.LENGTH_LONG).show()
            }
        }

        fun bind(item: Place) {
            binding.apply {
                place = item
                executePendingBindings()
            }
        }
    }

    var mViewLocateBtnClickListener : OnViewLocateBtnClickListener? = null
    fun setOnViewLocateBtnClickListener(listener: OnViewLocateBtnClickListener) {
        mViewLocateBtnClickListener = listener
    }
}

interface OnViewLocateBtnClickListener {
    fun OnButtonClick(view: View, place: Place)
}

private class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {

    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }
}