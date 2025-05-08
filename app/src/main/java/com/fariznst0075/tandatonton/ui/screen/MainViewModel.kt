package com.fariznst0075.tandatonton.ui.screen

import androidx.lifecycle.ViewModel
import com.fariznst0075.tandatonton.model.Film

class MainViewModel : ViewModel() {

    val data = listOf(
        Film(
            1,
            "Vinland Saga S1",
            "2025-02-17 12:34:56",
            "action"
        ),
        Film(
            2,
            "Prison School",
            "2025-02-19 12:34:56",
            "comedy"
        ),
        Film(
            3,
            "Prison School",
            "2025-02-23 12:34:56",
            "action"
        ),
        Film(
            4,
            "Prison School",
            "2025-02-26 12:34:56",
            "action"
        ),
        Film(
            5,
            "Prison School",
            "2025-03-03 12:34:56",
            "action"
        ),
        Film(
            6,
            "Vinland Saga S1",
            "2025-03-05 12:34:56",
            "action"
        )
    )
}