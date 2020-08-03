package com.mjjang.wheretogofirst.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mjjang.wheretogofirst.R
import androidx.databinding.DataBindingUtil.setContentView
import com.mjjang.wheretogofirst.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<ActivityMainBinding>(this,
        R.layout.activity_main)
    }
}