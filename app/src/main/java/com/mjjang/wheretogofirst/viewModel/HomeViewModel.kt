package com.mjjang.wheretogofirst.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjjang.wheretogofirst.data.Place
import com.mjjang.wheretogofirst.data.PlaceDao
import com.mjjang.wheretogofirst.manager.AppPreference
import com.mjjang.wheretogofirst.util.TYPE_PLACE_DEST
import com.mjjang.wheretogofirst.util.TYPE_PLACE_START
import com.mjjang.wheretogofirst.util.TYPE_PLACE_VIA
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel internal constructor(
    private val placeDao: PlaceDao
): ViewModel() {

    val startPlace: LiveData<List<Place>> = placeDao.getPlaceByRouteType(TYPE_PLACE_START)
    val viaPlace: LiveData<List<Place>> = placeDao.getPlaceByRouteType(TYPE_PLACE_VIA)
    val destPlace: LiveData<List<Place>> = placeDao.getPlaceByRouteType(TYPE_PLACE_DEST)

    fun getPlaceCountByRouteType(routeType: Int): Int {
        return placeDao.getPlaceCountByRouteType(routeType)
    }

    suspend fun getCount(): Int {
        val count = viewModelScope.async(Dispatchers.Default) {
            placeDao.getCount()
        }
        return count.await()
    }

    fun updateAll(place: List<Place>) {
        GlobalScope.launch(Dispatchers.IO) {
            placeDao.updateAll(place)
        }
    }

    fun insertPlace(place: Place) {
        GlobalScope.launch(Dispatchers.IO) {
            place.sid = AppPreference.getSid()
            placeDao.insert2(place)
        }
    }
}