package com.mjjang.wheretogofirst.ui

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mjjang.wheretogofirst.adapter.HomePlaceListAdapter
import com.mjjang.wheretogofirst.data.Place
import com.mjjang.wheretogofirst.databinding.FragmentHomeBinding
import com.mjjang.wheretogofirst.manager.AppPreference
import com.mjjang.wheretogofirst.manager.PermissionManager
import com.mjjang.wheretogofirst.network.RetrofitManager
import com.mjjang.wheretogofirst.util.*
import com.mjjang.wheretogofirst.viewModel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        context ?: return binding.root

        PermissionManager.checkPermissionWhenOnCreate(requireActivity())

        // init recycler
        val startAdapter = HomePlaceListAdapter()
        binding.listStartPoint.adapter = startAdapter
        homeViewModel.startPlace.observe(viewLifecycleOwner) {
            startAdapter.submitList(it)
        }

        val viaAdapter = HomePlaceListAdapter()
        binding.listViaPoint.adapter = viaAdapter
        homeViewModel.viaPlace.observe(viewLifecycleOwner) {
            viaAdapter.submitList(it)
        }

        val destAdapter = HomePlaceListAdapter()
        binding.listDestPoint.adapter = destAdapter
        homeViewModel.destPlace.observe(viewLifecycleOwner) {
            destAdapter.submitList(it)
        }

        // init button
        binding.btnAddStartPlace.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            intent.putExtra(INTENT_KEY_PLACE_ROUTE_TYPE, TYPE_PLACE_START)
            startActivity(intent)
        }

        binding.btnAddViaPlace.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            intent.putExtra(INTENT_KEY_PLACE_ROUTE_TYPE, TYPE_PLACE_VIA)
            startActivity(intent)
        }

        binding.btnAddDestPlace.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            intent.putExtra(INTENT_KEY_PLACE_ROUTE_TYPE, TYPE_PLACE_DEST)
            startActivity(intent)
        }


        binding.btnGetLocateStartPlace.setOnClickListener {
            addItemByGetlocation(TYPE_PLACE_START)
        }

        binding.btnGetLocateDestPlace.setOnClickListener {
            addItemByGetlocation(TYPE_PLACE_DEST)
        }

        binding.btnRoute.setOnClickListener {
            showStartRouteDialog()
        }

        binding.btnDeleteAll.setOnClickListener {
            deletePlaceAll()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PermissionManager.GPS_ENABLE_REQUEST_CODE -> {
                if (PermissionManager.checkLocationServicesStatus(requireContext())) {
                    PermissionManager.checkRunTimePermission(requireActivity())
                }
            }
        }
    }

    fun addItemByGetlocation(routeType: Int) {
        val gpsTracker = GpsTracker(requireContext())
        val address = PermissionManager.getCurrentAddress(requireContext(), gpsTracker.getLatitude(), gpsTracker.getLongitude())

        gpsTracker.longitude?.let { it1 ->
            gpsTracker.latitude?.let { it2 ->
                lifecycleScope.launch(Dispatchers.Default) {
                    val place = Place(-1, null, address, null, null, null,
                        null, address, address, it1, it2, null, null, routeType, 0)

                    homeViewModel.insertPlace(place)
                }
            }
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

                val startPlace = homeViewModel.startPlace.value?.get(0)
                val viaPlace = homeViewModel.viaPlace.value
                val destPlace = homeViewModel.destPlace.value?.get(0)

                var location = "${startPlace!!.x},${startPlace.y}"

                if (viaPlace != null) {
                    for (item in viaPlace) {
                        location += ";${item.x},${item.y}"
                    }
                }

                location += ";${destPlace!!.x},${destPlace.y}"

                // start routing
                RetrofitManager.getOsrmService().getRoute(location).apply {
                    this.body()?.let {
                        Log.d("mjjang test", "탐색 완료, size : ${it.waypoints.size}")
                        for (index in 1..it.waypoints.size - 2) {
                            viaPlace?.get(index-1)?.waypointIdx = it.waypoints[index].waypointIdx
                        }
                        startPlace.waypointIdx = 0
                        destPlace.waypointIdx = viaPlace?.size?.plus(1) ?: it.waypoints.size
                    }

                    val places = ArrayList<Place>()
                    places.add(startPlace)
                    viaPlace?.let { places.addAll(it) }
                    places.add(destPlace)

                    homeViewModel.updateAll(places)

                    withContext(Dispatchers.Main) {
                        navigateToResult(this@HomeFragment.requireView())
                    }
                }
            } catch (e: Throwable) {
                e.stackTrace
            }
        }
    }

    private fun navigateToResult(view: View) {
        val direction = HomeFragmentDirections.actionFragmentHomeToFragmentResult()
        view.findNavController().navigate(direction)
    }

    private fun deletePlaceAll() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("장소 모두 삭제")
            .setMessage("설정된 장소를 모두 삭제 하시겠습니까?")
            .setCancelable(true)
            .setPositiveButton("예", DialogInterface.OnClickListener { dialogInterface, i ->
                lifecycleScope.launch(Dispatchers.IO) {
                    homeViewModel.deleteAll()
                }
            })
            .setNegativeButton("아니오", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
            })
            .show()
    }
}