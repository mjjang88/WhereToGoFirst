package com.mjjang.wheretogofirst.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OsrmWaypoint(
    @SerializedName("waypoint_index")
    @Expose
    val waypointIdx: Int,
    @SerializedName("trips_index")
    @Expose
    val tripsIdx: Int,
    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("distance")
    @Expose
    val distance: Double
) {
}