package com.mjjang.wheretogofirst.data

data class Place(
    val id : Long,
    val name : String?,
    val categoryName : String?,
    val categoryGroupCode : String?,
    val categoryGroupName : String?,
    val phone : String?,
    val address : String?,
    val address_road : String?,
    val x : Double,
    val y : Double,
    val url : String?,
    val distance : Int
) {
}