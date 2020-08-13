package com.mjjang.wheretogofirst.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjjang.wheretogofirst.data.Place
import com.mjjang.wheretogofirst.network.RetrofitManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchPoiViewModel internal constructor(
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
}