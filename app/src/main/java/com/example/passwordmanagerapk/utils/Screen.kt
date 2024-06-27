package com.example.warehousetracebilityandroid.utils

sealed class Screen(val route: String) {
    object HomeScreen : Screen("HomeScreen")

}
