package com.mjjang.wheretogofirst.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjjang.wheretogofirst.data.Place
import com.mjjang.wheretogofirst.data.PlaceDao
import com.mjjang.wheretogofirst.manager.AppPreference
import com.mjjang.wheretogofirst.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchPoiViewModel internal constructor(
    private val placeDao: PlaceDao
) : ViewModel() {

    val searchWord: MutableLiveData<String?> = MutableLiveData("")
    val placeList = MutableLiveData<ArrayList<Place>>().apply { value = arrayListOf()}

    init{
        getPlaceList()
    }

    fun getPlaceList() {

        val word = searchWord.value
        if (word.isNullOrBlank()) {
            return
        }

        placeList.value?.clear()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                RetrofitManager.getKaKaoService().getPlace(word).apply {
                    this.body()?.let {
                        placeList.value?.addAll(it.documents)
                    }

                    withContext(Dispatchers.Main) {
                        placeList.value = placeList.value
                    }
                }
            } catch (e: Throwable) {
                e.stackTrace
            }
        }
    }

    fun insertPlace(place: Place) {
        GlobalScope.launch(Dispatchers.IO) {
            place.sid = AppPreference.getSid()
            placeDao.insert2(place)
        }
    }

}