package com.mjjang.wheretogofirst.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mjjang.wheretogofirst.data.Place
import com.mjjang.wheretogofirst.data.PlaceDao
import com.mjjang.wheretogofirst.util.TYPE_PLACE_DEST
import com.mjjang.wheretogofirst.util.TYPE_PLACE_START
import com.mjjang.wheretogofirst.util.TYPE_PLACE_VIA

class HomeViewModel internal constructor(
    private val placeDao: PlaceDao
): ViewModel() {

    val startPlace: LiveData<List<Place>> = placeDao.getPlaceByRouteType(TYPE_PLACE_START)
    val viaPlace: LiveData<List<Place>> = placeDao.getPlaceByRouteType(TYPE_PLACE_VIA)
    val destPlace: LiveData<List<Place>> = placeDao.getPlaceByRouteType(TYPE_PLACE_DEST)

}