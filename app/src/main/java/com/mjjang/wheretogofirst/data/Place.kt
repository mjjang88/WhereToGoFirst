package com.mjjang.wheretogofirst.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Place(
    @SerializedName("id")
    @Expose
    val id : Long,
    @SerializedName("place_name")
    @Expose
    val name : String?,
    @SerializedName("category_name")
    @Expose
    val categoryName : String?,
    @SerializedName("category_group_code")
    @Expose
    val categoryGroupCode : String?,
    @SerializedName("category_group_name")
    @Expose
    val categoryGroupName : String?,
    @SerializedName("phone")
    @Expose
    val phone : String?,
    @SerializedName("address_name")
    @Expose
    val address : String?,
    @SerializedName("road_address_name")
    @Expose
    val address_road : String?,
    @SerializedName("x")
    @Expose
    val x : Double,
    @SerializedName("y")
    @Expose
    val y : Double,
    @SerializedName("place_url")
    @Expose
    val url : String?,
    @SerializedName("distance")
    @Expose
    val distance : String?
) : Parcelable{
}