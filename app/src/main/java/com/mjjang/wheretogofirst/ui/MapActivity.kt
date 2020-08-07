package com.mjjang.wheretogofirst.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.mjjang.wheretogofirst.R
import com.mjjang.wheretogofirst.adapter.PlaceListAdapter
import com.mjjang.wheretogofirst.databinding.ActivityMainBinding
import com.mjjang.wheretogofirst.viewModel.SearchPoiViewModel
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import kotlinx.android.synthetic.main.activity_map.*
import org.koin.android.ext.android.inject

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mNaverMap : NaverMap

    private val searchPoiViewModel: SearchPoiViewModel by inject()

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

        initList()
    }

    override fun onMapReady(p0: NaverMap) {
        mNaverMap = p0
    }

    private fun initList() {
        val adapter = PlaceListAdapter()
        list_place.adapter = adapter

        searchPoiViewModel.placeList.observe(this) { places ->
            adapter.submitList(places)
        }
    }
}