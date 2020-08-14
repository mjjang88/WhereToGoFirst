package com.mjjang.wheretogofirst.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlaceDao {

    @Query("SELECT COUNT(*) FROM place")
    fun getCount(): Int

    @Query("SELECT * FROM place WHERE routeType = :routeType ORDER BY sid asc")
    fun getPlaceByRouteType(routeType: Int): LiveData<List<Place>>

    @Query("SELECT count(*) FROM place WHERE routeType = :routeType")
    fun getPlaceCountByRouteType(routeType: Int): LiveData<List<Place>>

    @Query("DELETE * FROM place WHERE routeType = :routeType")
    suspend fun deletePlaceByRouteType

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Place)

    @Delete
    suspend fun delete(place: Place)

    
}