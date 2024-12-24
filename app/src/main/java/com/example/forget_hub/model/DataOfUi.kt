package com.example.forget_hub.model

import androidx.compose.ui.graphics.Color
import com.example.forget_hub.R

class DataOfUi {

    // use image in firstTimeDataSaveScreen
    fun image(): List<Int> {
        val illustrationImage = listOf(
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e,
            R.drawable.f,
            R.drawable.g,
            R.drawable.h,
            R.drawable.i,
            R.drawable.j,
            R.drawable.k,
            R.drawable.m,
            R.drawable.n,
            R.drawable.o,
        )

        return illustrationImage
    }


    // change header and other part of colours
    fun backgroundColourChange(): List<Color> {
        val colors = listOf(
            Color(0xffBFD641),
            Color(0xFFEF9A9A),
            Color(0xFF80DEEA),
            Color(0xffE0B589),
            Color(0xFF56C6A9),
            Color(0xFFD19C97),
            Color(0xFFD2C29D),
            Color(0xFFB39DDB),
            Color(0xFF80DEEA),
            Color(0xFFC5E1A5),
            Color(0xFFC0AB8E),
            Color(0xFFBE9EC9),
            Color(0xFF95DEE3),
            Color(0xFF80CBC4),
            Color(0xFF9FA8DA),
            Color(0xFF90CAF9),
            Color(0xFFB39DDB),
            Color(0xFFA5D6A7),
            Color(0xFF9FA8DA),
            Color(0xFFA5D6A7),
            Color(0xFFB39DDB),
            Color(0xFFC5E1A5),
            Color(0xFF80CBC4),
            Color(0xFF9FA8DA),
        )

        return colors
    }
}