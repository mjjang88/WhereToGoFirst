package com.mjjang.wheretogofirst.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mjjang.wheretogofirst.data.Place
import com.mjjang.wheretogofirst.data.PlaceDao

class ResultViewModel internal constructor(
    private val placeDao: PlaceDao
): ViewModel() {

    val place: LiveData<List<Place>> = placeDao.getPlace()
}