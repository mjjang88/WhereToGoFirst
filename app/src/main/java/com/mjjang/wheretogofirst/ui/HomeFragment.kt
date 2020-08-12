package com.mjjang.wheretogofirst.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.mjjang.wheretogofirst.data.Place
import com.mjjang.wheretogofirst.databinding.FragmentHomeBinding
import com.mjjang.wheretogofirst.manager.PermissionManager
import com.mjjang.wheretogofirst.util.GpsTracker
import com.mjjang.wheretogofirst.util.INTENT_KEY_RETURN_PLACE
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    val PLACE_REQUEST_CODE = 1001

    val IDX_PLACE_START = 0x00000
    val IDX_PLACE_VIA = 0x00001
    val IDX_PLACE_DEST = 0x00002
    var ADD_PLACE_IDX: Int = IDX_PLACE_DEST

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        context ?: return binding.root

        PermissionManager.checkPermissionWhenOnCreate(requireActivity())

        binding.btnGetLocate.setOnClickListener {
            val gpsTracker = GpsTracker(requireContext())

            val address = PermissionManager.getCurrentAddress(requireContext(), gpsTracker.getLatitude(), gpsTracker.getLongitude())

            Toast.makeText(requireContext(), "현재위치 \n위도: ${gpsTracker.latitude}\n경도: ${gpsTracker.longitude}\n주소: $address", Toast.LENGTH_LONG).show()
        }

        binding.btnAddPlace.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivityForResult(intent, PLACE_REQUEST_CODE)
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
            PlaceListView(requireContext(), layout_start_point, it)
        }
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
}