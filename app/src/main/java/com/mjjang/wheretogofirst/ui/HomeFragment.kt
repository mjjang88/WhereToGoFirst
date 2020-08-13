package com.mjjang.wheretogofirst.ui

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mjjang.wheretogofirst.data.Place
import com.mjjang.wheretogofirst.databinding.FragmentHomeBinding
import com.mjjang.wheretogofirst.manager.PermissionManager
import com.mjjang.wheretogofirst.network.RetrofitManager
import com.mjjang.wheretogofirst.util.GpsTracker
import com.mjjang.wheretogofirst.util.INTENT_KEY_RETURN_PLACE
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    val PLACE_REQUEST_CODE = 1001

    val IDX_PLACE_START = 0x00000
    val IDX_PLACE_VIA = 0x00001
    val IDX_PLACE_DEST = 0x00002
    var mAddPlaceIdx: Int = IDX_PLACE_DEST

    var startPlace: Place? = null
    var viaPlace: ArrayList<Place> = ArrayList()
    var destPlace: Place? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        context ?: return binding.root

        PermissionManager.checkPermissionWhenOnCreate(requireActivity())

        binding.btnAddStartPlace.setOnClickListener {
            mAddPlaceIdx = IDX_PLACE_START
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivityForResult(intent, PLACE_REQUEST_CODE)
        }

        binding.btnAddViaPlace.setOnClickListener {
            mAddPlaceIdx = IDX_PLACE_VIA
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivityForResult(intent, PLACE_REQUEST_CODE)
        }

        binding.btnAddDestPlace.setOnClickListener {
            mAddPlaceIdx = IDX_PLACE_DEST
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivityForResult(intent, PLACE_REQUEST_CODE)
        }

        binding.btnGetLocateStartPlace.setOnClickListener {
            mAddPlaceIdx = IDX_PLACE_START
            addItemByGetlocation()
        }

        binding.btnGetLocateDestPlace.setOnClickListener {
            mAddPlaceIdx = IDX_PLACE_DEST
            addItemByGetlocation()
        }

        binding.btnRoute.setOnClickListener {
            showStartRouteDialog()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PLACE_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val place = data?.getParcelableExtra<Place>(INTENT_KEY_RETURN_PLACE)
                    Toast.makeText(requireContext(), place?.name, Toast.LENGTH_SHORT).show()

                    addItem(place)
                }
            }
            PermissionManager.GPS_ENABLE_REQUEST_CODE -> {
                if (PermissionManager.checkLocationServicesStatus(requireContext())) {
                    PermissionManager.checkRunTimePermission(requireActivity())
                }
            }
        }
    }

    fun addItem(place: Place?) {
        place?.let {
            when (mAddPlaceIdx) {
                IDX_PLACE_START -> {
                    if (startPlace != null) {
                        layout_start_point.removeAllViews()
                    }
                    startPlace = it
                    PlaceListView(requireContext(), layout_start_point, it).setPlaceItemClickListener(object: OnPlaceItemClickListener{
                        override fun onItemClick(view: View, place: Place) {
                            startPlace = null
                            layout_start_point.removeAllViews()
                        }
                    })

                }
                IDX_PLACE_VIA -> {
                    viaPlace.add(place)
                    PlaceListView(requireContext(), layout_via_point, it).setPlaceItemClickListener(object: OnPlaceItemClickListener{
                        override fun onItemClick(view: View, place: Place) {
                            val index = viaPlace.indexOf(place)
                            viaPlace.removeAt(index)
                            layout_via_point.removeViewAt(index)
                        }
                    })
                }
                IDX_PLACE_DEST -> {
                    if (destPlace != null) {
                        layout_dest_point.removeAllViews()
                    }
                    destPlace = it
                    PlaceListView(requireContext(), layout_dest_point, it).setPlaceItemClickListener(object: OnPlaceItemClickListener{
                        override fun onItemClick(view: View, place: Place) {
                            destPlace = null
                            layout_dest_point.removeAllViews()
                        }
                    })
                }
                else -> {
                }
            }

        }
    }

    fun addItemByGetlocation() {
        val gpsTracker = GpsTracker(requireContext())
        val address = PermissionManager.getCurrentAddress(requireContext(), gpsTracker.getLatitude(), gpsTracker.getLongitude())

        val place = gpsTracker.longitude?.let { it1 ->
            gpsTracker.latitude?.let { it2 ->
                Place(null, address, null, null, null,
                    null, address, address, it1, it2, null, null)
            }
        }

        addItem(place)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PermissionManager.PERMISSIONS_REQUEST_CODE &&
                grantResults.size == PermissionManager.REQUIRED_PERMISSIONS.size) {

            var check_result = true

            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }

            if (!check_result) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), PermissionManager.REQUIRED_PERMISSIONS[0]) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), PermissionManager.REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(requireContext(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun showStartRouteDialog() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("경로 탐색")
            .setMessage("설정한 장소를 사용하여 최적의 경로를 탐색하시겠습니까?")
            .setCancelable(true)
            .setPositiveButton("시작", DialogInterface.OnClickListener { dialogInterface, i ->
                doRouting()
            })
            .setNegativeButton("취소", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
            })
            .show()
    }

    fun doRouting() {

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val location = makeLocationStringByPlaceList()
                RetrofitManager.getOsrmService().getRoute(location).apply {
                    this.body()?.let {
                        Log.d("mjjang test", "탐색 완료, size : ${it.waypoints.size}")
                    }

                    withContext(Dispatchers.Main) {

                    }
                }
            } catch (e: Throwable) {
                e.stackTrace
            }
        }

    }

    fun makeLocationStringByPlaceList(): String {

        val location = "${startPlace!!.x},${startPlace!!.y}"

        for (item in viaPlace) {
            location.plus(";${item.x},${item.y}")
        }

        location.plus(";${destPlace!!.x},${destPlace!!.y}")

        return location
    }
}