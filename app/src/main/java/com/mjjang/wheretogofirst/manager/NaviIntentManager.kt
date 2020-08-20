package com.mjjang.wheretogofirst.manager

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import com.kakao.sdk.navi.NaviClient
import com.kakao.sdk.navi.model.CoordType
import com.kakao.sdk.navi.model.Location
import com.kakao.sdk.navi.model.NaviOptions
import com.mjjang.wheretogofirst.data.Place
import java.lang.StringBuilder


object NaviIntentManager {

    fun startTmapNavi(context: Context, place: Place) {
        val intent = Intent (Intent.ACTION_VIEW, Uri.parse("tmap://route?goalx=${place.x}&goaly=${place.y}&goalname=${place.name}")).apply {
            `package` = "com.skt.skaf.l001mtm091"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun startTmapCommonNavi(context: Context, place: Place) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tmap://route?goalx=${place.x}&goaly=${place.y}&goalname=${place.name}")).apply {
            `package` = "com.skt.tmap.ku"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun startNaverNavi(context: Context, place: Place) {

        val url = "nmap://navigation?dlat=${place.y}&dlng=${place.x}&dname=${place.name}&appname=com.mjjang.wheretogofirst"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addCategory(Intent.CATEGORY_BROWSABLE)

        val list: List<ResolveInfo> =
            context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list == null || list.isEmpty()) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.nhn.android.nmap")
                )
            )
        } else {
            context.startActivity(intent)
        }
    }

    fun startKakaoNavi(context: Context, place: Place) {

        if (NaviClient.instance.isKakaoNaviInstalled(context)) {
            context.startActivity(
                NaviClient.instance.navigateIntent(
                    Location(place.name.toString(), place.x, place.y),
                    NaviOptions(coordType = CoordType.WGS84)
                )
            )
        } else {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.locnall.KimGiSa")
                )
            )
        }
    }

    fun startNaverMapWithVia(context: Context, places: List<Place>) {

        val placeSize = places.size
        var strParam = ""
        for ((index, value) in places.withIndex()) {
            if (index >= placeSize - 1 || index >= 6) {
                strParam += "dlat=${value.y}&dlng=${value.x}&dname=${value.name}"
                break
            }
            when (index) {
                0 -> strParam += "slat=${value.y}&slng=${value.x}&sname=${value.name}"
                1 -> strParam += "v1lat=${value.y}&v1lng=${value.x}&v1name=${value.name}"
                2 -> strParam += "v2lat=${value.y}&v2lng=${value.x}&v2name=${value.name}"
                3 -> strParam += "v3lat=${value.y}&v3lng=${value.x}&v3name=${value.name}"
                4 -> strParam += "v4lat=${value.y}&v4lng=${value.x}&v4name=${value.name}"
                5 -> strParam += "v5lat=${value.y}&v5lng=${value.x}&v5name=${value.name}"
            }
            strParam += "&"
        }

        val url = "nmap://route/car?${strParam}&appname=com.mjjang.wheretogofirst"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addCategory(Intent.CATEGORY_BROWSABLE)

        val list: List<ResolveInfo> =
            context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list == null || list.isEmpty()) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.nhn.android.nmap")
                )
            )
        } else {
            context.startActivity(intent)
        }
    }
}