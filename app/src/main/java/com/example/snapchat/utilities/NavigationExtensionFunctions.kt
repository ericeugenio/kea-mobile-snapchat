package com.example.snapchat.utilities

import androidx.navigation.NavHostController

fun NavHostController.popUpToAndNavigate(
    route: String,
    popUpRoute: String
) {
    this.navigate(route) {
        launchSingleTop = true
        popUpTo(popUpRoute)
    }
}

fun NavHostController.clearBackStackAndNavigate(
    route: String
) {
    val initialDestination = this.graph.id

    this.navigate(route) {
        launchSingleTop = true
        popUpTo(initialDestination)
    }
}
