package com.mjjang.wheretogofirst.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.mjjang.wheretogofirst.adapter.ResultListAdapter
import com.mjjang.wheretogofirst.databinding.FragmentResultBinding
import com.mjjang.wheretogofirst.viewModel.ResultViewModel
import org.koin.android.ext.android.inject

class ResultFragment: Fragment() {

    private val resultViewModel: ResultViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentResultBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = ResultListAdapter()
        binding.listResult.adapter = adapter
        resultViewModel.place.observe(viewLifecycleOwner) { place ->
            adapter.submitList(place)
        }

        return binding.root
    }
}