package com.mjjang.wheretogofirst.data

import com.mjjang.wheretogofirst.manager.MyApplication
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object DBProcedure {

    fun deletePlace(place: Place) {
        GlobalScope.launch {
            AppDatabase.getInstance(MyApplication.applicationContext()).placeDao().delete(place)
        }
    }

}