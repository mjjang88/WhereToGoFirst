package com.mjjang.wheretogofirst.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mjjang.wheretogofirst.data.Place
import com.mjjang.wheretogofirst.databinding.FragmentHomeBinding
import com.mjjang.wheretogofirst.util.INTENT_KEY_RETURN_PLACE
import com.mjjang.wheretogofirst.util.REQUEST_CODE_RETURN_PLACE

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.btnAddPlace.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_RETURN_PLACE)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_RETURN_PLACE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val place = data?.getParcelableExtra<Place>(INTENT_KEY_RETURN_PLACE)
                    Toast.makeText(requireContext(), place?.name, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}