package com.mjjang.wheretogofirst.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mjjang.wheretogofirst.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMapBinding.inflate(inflater, container, false)
        context ?: return binding.root

        return binding.root
    }
}