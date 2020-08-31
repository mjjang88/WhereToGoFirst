package com.mjjang.wheretogofirst.manager

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kakao.sdk.navi.NaviClient
import com.kakao.sdk.navi.model.CoordType
import com.kakao.sdk.navi.model.Location
import com.kakao.sdk.navi.model.NaviOptions
import com.mjjang.wheretogofirst.data.Place


object NaviIntentManager {

    fun startTmapNavi(context: Context, place: Place) {
        val intent = Intent (Intent.ACTION_VIEW, Uri.parse("tmap://route?goalx=${place.x}&goaly=${place.y}&goalname=${place.name}")).apply {
            `package` = "com.skt.skaf.l001mtm091"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val list: List<ResolveInfo> =
            context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list == null || list.isEmpty()) {
            MaterialAlertDialogBuilder(context)
                .setTitle("미 설치 안내")
                .setMessage("TMap이 미설치 되어 있습니다.")
                .setCancelable(true)
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                })
                .show()
        } else {
            context.startActivity(intent)
        }
    }

    fun startTmapCommonNavi(context: Context, place: Place) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tmap://route?goalx=${place.x}&goaly=${place.y}&goalname=${place.name}")).apply {
            `package` = "com.skt.tmap.ku"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val list: List<ResolveInfo> =
            context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list == null || list.isEmpty()) {
            showInstallAppDialog(context, "티맵 (공용앱)", "market://details?id=com.skt.tmap.ku")
        } else {
            context.startActivity(intent)
        }
    }

    fun startNaverNavi(context: Context, place: Place) {

        val url = "nmap://navigation?dlat=${place.y}&dlng=${place.x}&dname=${place.name}&appname=com.mjjang.wheretogofirst"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addCategory(Intent.CATEGORY_BROWSABLE)

        val list: List<ResolveInfo> =
            context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list == null || list.isEmpty()) {
            showInstallAppDialog(context, "네이버", "market://details?id=com.nhn.android.nmap")
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
            showInstallAppDialog(context, "카카오내비", "market://details?id=com.locnall.KimGiSa")
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
            showInstallAppDialog(context, "네이버", "market://details?id=com.nhn.android.nmap")
        } else {
            context.startActivity(intent)
        }
    }

    fun showInstallAppDialog(context: Context, appName: String, uri: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle("$appName 앱 설치 안내")
            .setMessage("Play 스토어로 이동하여 설치하시겠습니까?")
            .setCancelable(true)
            .setPositiveButton("예") { _, _ ->
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(uri)
                    )
                )
            }
            .setNegativeButton("아니오") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .show()
    }
}