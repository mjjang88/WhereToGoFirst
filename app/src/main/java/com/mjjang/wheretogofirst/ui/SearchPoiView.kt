package com.mjjang.wheretogofirst.ui

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.mjjang.wheretogofirst.adapter.PlaceListAdapter
import com.mjjang.wheretogofirst.databinding.ViewSearchPoiBinding

class SearchPoiView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
/*class SearchPoiView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
    : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {*/

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ViewSearchPoiBinding.inflate(inflater, this, false)

        addView(binding.root)

        initList(binding)
    }

    private fun initList(binding: ViewSearchPoiBinding) {
        val adapter = PlaceListAdapter()
        binding.listPlace.adapter = adapter

        /*viewModel.places.observe((context as Activity).viewLifecycleOwner) { places ->
            adapter.submitList(places)
        }*/
    }
}