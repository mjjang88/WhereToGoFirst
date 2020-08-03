package com.mjjang.wheretogofirst.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.mjjang.wheretogofirst.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.btnAddPlace.setOnClickListener {
            navigateToMap(it)
        }

        return binding.root
    }

    fun navigateToMap(view: View) {
        val direction =
            HomeFragmentDirections.actionFragmentHomeToFragmentMap()
        view.findNavController().navigate(direction)
    }
}