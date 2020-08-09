package com.mjjang.wheretogofirst.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mjjang.wheretogofirst.R
import com.mjjang.wheretogofirst.adapter.OnViewLocateBtnClickListener
import com.mjjang.wheretogofirst.adapter.PlaceListAdapter
import com.mjjang.wheretogofirst.data.Place
import com.mjjang.wheretogofirst.databinding.ActivityMainBinding
import com.mjjang.wheretogofirst.databinding.ActivityMapBinding
import com.mjjang.wheretogofirst.manager.KeyboardManager
import com.mjjang.wheretogofirst.viewModel.SearchPoiViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import kotlinx.android.synthetic.main.activity_map.*
import org.koin.android.ext.android.inject

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mNaverMap : NaverMap
    var markerList: ArrayList<Marker> = ArrayList()

    private val searchPoiViewModel: SearchPoiViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMapBinding>(
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
        initEditText()
        initViewStyleCheckBox()
    }

    override fun onMapReady(p0: NaverMap) {
        mNaverMap = p0
    }

    private fun initList() {
        val adapter = PlaceListAdapter()
        adapter.setOnViewLocateBtnClickListener(object: OnViewLocateBtnClickListener{
            override fun OnButtonClick(view: View, place: Place) {
                val cameraUpdate = CameraUpdate.scrollTo(LatLng(place.y, place.x))
                mNaverMap.moveCamera(cameraUpdate)

                checkbox_view_style.isChecked = true
            }
        })

        list_place.adapter = adapter
        list_place.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        searchPoiViewModel.placeList.observe(this) { places ->
            adapter.submitList(places.toList())
            updateMapMarker(places)
        }
    }

    private fun updateMapMarker(places: List<Place>) {
        markerList.map { it.map = null }

        places.forEach {
            val marker = Marker()
            marker.captionText = it.name.toString()
            marker.position = LatLng(it.y, it.x)
            marker.map = mNaverMap
            markerList.add(marker)
        }

        if (places.isNotEmpty()) {
            val cameraUpdate = CameraUpdate.scrollTo(LatLng(places[0].y, places[0].x))
            mNaverMap.moveCamera(cameraUpdate)
        }
    }

    private fun initEditText() {
        edit_input_search_word.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    searchPoiViewModel.searchWord.value = v.text.toString()
                    searchPoiViewModel.getPlaceList()
                    v.clearFocus()
                    KeyboardManager.hideKeyboard(this, v)
                    true
                }
                else -> false
            }
        }
    }

    private fun initViewStyleCheckBox() {
        checkbox_view_style.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {
                true -> list_place.visibility = View.GONE
                false -> list_place.visibility = View.VISIBLE
            }
        }
    }
}