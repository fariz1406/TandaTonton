package com.fariznst0075.tandatonton.ui.screen

import androidx.lifecycle.ViewModel
import com.fariznst0075.tandatonton.model.Film

class MainViewModel : ViewModel() {

    val data = listOf(
        Film(
            1,
            "Vinland Saga S1",
            "series",
            "selesai",
            "2025-02-17 12:34:56"

        ),
        Film(
            2,
            "Prison School",
            "series",
            "selesai",
            "2025-02-17 12:34:56"

        ),
        Film(
            3,
            "Prison School",
            "series",
            "selesai",
            "2025-02-17 12:34:56"

        ),
        Film(
            4,
            "Prison School",
            "series",
            "selesai",
            "2025-02-17 12:34:56"

        ),
        Film(
            5,
            "Prison School",
            "series",
            "selesai",
            "2025-02-17 12:34:56"

        ),
        Film(
            6,
            "Vinland Saga S1",
            "series",
            "selesai",
            "2025-02-17 12:34:56"

        )
    )

    fun getFilm(id: Long): Film? {
        return data.find { it.id == id }
    }
}