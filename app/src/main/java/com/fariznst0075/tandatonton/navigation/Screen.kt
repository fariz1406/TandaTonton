package com.fariznst0075.tandatonton.navigation

import com.fariznst0075.tandatonton.ui.screen.KEY_ID_CATATAN

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object FormBaru: Screen("detailScreen")
    data object FormUbah: Screen("detailscreen/{$KEY_ID_CATATAN}"){
        fun withId (id: Long) = "detailscreen/$id"
    }
}