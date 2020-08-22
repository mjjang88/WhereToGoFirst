package com.mjjang.wheretogofirst.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mjjang.wheretogofirst.util.TYPE_PLACE_DEST
import com.mjjang.wheretogofirst.util.TYPE_PLACE_START

@Dao
interface PlaceDao {

    @Query("SELECT COUNT(*) FROM place")
    fun getCount(): Int

    @Query("SELECT * FROM place ORDER BY waypointIdx asc")
    fun getPlace(): LiveData<List<Place>>

    @Query("SELECT * FROM place WHERE routeType = :routeType ORDER BY sid asc")
    fun getPlaceByRouteType(routeType: Int): LiveData<List<Place>>

    @Query("SELECT count(*) FROM place WHERE routeType = :routeType")
    fun getPlaceCountByRouteType(routeType: Int): Int

    @Query("DELETE FROM place WHERE routeType = :routeType")
    suspend fun deletePlaceByRouteType(routeType: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Place)

    @Delete
    suspend fun delete(place: Place)

    @Query("DELETE FROM place")
    suspend fun deleteAll()

    @Update
    suspend fun updateAll(place: List<Place>)

    @Transaction
    suspend fun insert2(place: Place) {
        val routeType = place.routeType
        if (routeType == TYPE_PLACE_START || routeType == TYPE_PLACE_DEST) {
            deletePlaceByRouteType(routeType)
        }
        insert(place)
    }
}