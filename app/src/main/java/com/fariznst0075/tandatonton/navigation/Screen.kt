package com.fariznst0075.tandatonton.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
}