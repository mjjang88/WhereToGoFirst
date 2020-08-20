package com.mjjang.wheretogofirst.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mjjang.wheretogofirst.data.Place
import com.mjjang.wheretogofirst.databinding.ListItemResultBinding
import com.mjjang.wheretogofirst.manager.NaviIntentManager

class ResultListAdapter() : ListAdapter<Place, RecyclerView.ViewHolder>(ResultListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ResultViewHolder(
            ListItemResultBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val place = getItem(position)
        (holder as ResultViewHolder).bind(place)
    }

    class ResultViewHolder(
        private val binding: ListItemResultBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Place) {
            binding.apply {
                place = item
                executePendingBindings()

                btnStartNavi.setOnClickListener {
                    doStartNavi(item)
                }
            }
        }

        fun doStartNavi(item: Place) {

            val items = arrayOf("티맵 (전용앱)", "티맵 (공용앱)", "네이버", "카카오")

            val context = binding.root.context

            MaterialAlertDialogBuilder(context)
                .setTitle("목적지로 내비게이션 연결")
                .setItems(items) { dialogInterface, i ->
                    when (items[i]) {
                        "티맵 (전용앱)" -> NaviIntentManager.startTmapNavi(context, item)
                        "티맵 (공용앱)" -> NaviIntentManager.startTmapCommonNavi(context, item)
                        "네이버" -> NaviIntentManager.startNaverNavi(context, item)
                        "카카오" -> NaviIntentManager.startKakaoNavi(context, item)
                    }
                }
                .show()
        }
    }
}

private class ResultListDiffCallback : DiffUtil.ItemCallback<Place>() {

    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }
}