package com.mjjang.wheretogofirst.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mjjang.wheretogofirst.R
import com.mjjang.wheretogofirst.databinding.ActivityMainBinding
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mNaverMap : NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_map
        )

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: NaverMap) {
        mNaverMap = p0
    }
}