package com.tenclouds.swipeablerecyclerviewcell

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class MockActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_cell_layout)
    }
}