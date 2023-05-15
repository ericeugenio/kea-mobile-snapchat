package com.example.snapchat

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.snapchat.ui.navigation.SnapchatNavGraph

@Composable
fun SnapchatApp(
    navController: NavHostController = rememberNavController(),
) {
    SnapchatNavGraph(navController = navController)
}