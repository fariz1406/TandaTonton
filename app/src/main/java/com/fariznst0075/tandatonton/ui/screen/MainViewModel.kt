package com.fariznst0075.tandatonton.ui.screen

import androidx.lifecycle.ViewModel
import com.fariznst0075.tandatonton.model.Film

class MainViewModel : ViewModel() {

    val data = listOf(
        Film(
            1,
            "Kuliah Mobpro 17 Feb",
            "2025-02-17 12:34:56",
            "action"
        ),
        Film(
            2,
            "Kuliah Mobpro 19 Feb",
            "2025-02-19 12:34:56",
            "comedy"
        ),
        Film(
            3,
            "Kuliah Mobpro 24 Feb",
            "2025-02-23 12:34:56",
            "action"
        ),
        Film(
            4,
            "Kuliah Mobpro 26 Feb",
            "2025-02-26 12:34:56",
            "action"
        ),
        Film(
            5,
            "Kuliah Mobpro 03 Mar",
            "2025-03-03 12:34:56",
            "action"
        ),
        Film(
            6,
            "Kuliah Mobpro 05 Mar",
            "2025-03-05 12:34:56",
            "action"
        )
    )
}