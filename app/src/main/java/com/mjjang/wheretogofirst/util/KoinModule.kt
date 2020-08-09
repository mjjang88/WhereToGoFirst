package com.mjjang.wheretogofirst.util

import com.mjjang.wheretogofirst.viewModel.SearchPoiViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    /**
     * Database Module
     */

    /**
     * ViewModel Module
     */
    viewModel { SearchPoiViewModel() }

}