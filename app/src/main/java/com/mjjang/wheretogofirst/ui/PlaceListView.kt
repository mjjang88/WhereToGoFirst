package com.mjjang.wheretogofirst.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mjjang.wheretogofirst.R
import com.mjjang.wheretogofirst.data.Place
import com.mjjang.wheretogofirst.databinding.ListItemHomePlaceBinding

class PlaceListView @JvmOverloads constructor(
    context: Context,
    parent: ViewGroup,
    place: Place,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr){

    val IDX_PLACE_START = 0x00000
    val IDX_PLACE_VIA = 0x00001
    val IDX_PLACE_DEST = 0x00002
    var mItemPlaceIdx = IDX_PLACE_START

    init {
        val bind = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.list_item_home_place, parent, true) as ListItemHomePlaceBinding
        bind.place = place

        when (parent.id) {
            R.id.layout_start_point -> mItemPlaceIdx = IDX_PLACE_START
            R.id.layout_via_point -> mItemPlaceIdx = IDX_PLACE_VIA
            R.id.layout_dest_point -> mItemPlaceIdx = IDX_PLACE_DEST
        }

        bind.btnDelete.setOnClickListener {
            mPlaceItemClickListener?.onItemClick(it, place)
        }
    }

    var mPlaceItemClickListener : OnPlaceItemClickListener? = null
    fun setPlaceItemClickListener(listener: OnPlaceItemClickListener) {
        mPlaceItemClickListener = listener
    }
}

interface OnPlaceItemClickListener {
    fun onItemClick(view: View, place: Place)
}