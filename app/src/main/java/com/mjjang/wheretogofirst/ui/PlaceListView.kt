package com.mjjang.wheretogofirst.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mjjang.wheretogofirst.R
import com.mjjang.wheretogofirst.data.Place
import com.mjjang.wheretogofirst.databinding.ViewPlaceListBinding

class PlaceListView @JvmOverloads constructor(
    context: Context,
    parent: ViewGroup,
    place: Place,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr){

    init {
        val bind = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_place_list, parent, true) as ViewPlaceListBinding
        bind.place = place
    }
}