package com.mjjang.wheretogofirst.util

import com.mjjang.wheretogofirst.data.AppDatabase
import com.mjjang.wheretogofirst.viewModel.HomeViewModel
import com.mjjang.wheretogofirst.viewModel.SearchPoiViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    /**
     * Database Module
     */
    single { AppDatabase.getInstance(androidApplication()) }
    single(createdAtStart = false) { get<AppDatabase>().placeDao() }

    /**
     * ViewModel Module
     */
    viewModel { SearchPoiViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}