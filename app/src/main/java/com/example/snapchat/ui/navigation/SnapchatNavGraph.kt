package com.example.snapchat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.snapchat.ui.screens.HomeScreen
import com.example.snapchat.ui.screens.auth.SignInScreen
import com.example.snapchat.ui.screens.auth.SignUpScreen
import com.example.snapchat.ui.screens.camera.CameraScreen
import com.example.snapchat.ui.screens.camera.ImagePreviewScreen
import com.example.snapchat.ui.screens.profile.ProfileScreen
import com.example.snapchat.ui.screens.splash.SplashScreen

@Composable
fun SnapchatNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = SnapchatRoutes.Splash.name,
        modifier = modifier
    ) {
        composable(route = SnapchatRoutes.Splash.name) {
            SplashScreen(
                navigateToSignIn = {
                    navController.popUpToInclusiveAndNavigate(
                        SnapchatRoutes.SignIn.name,
                        SnapchatRoutes.Splash.name
                    )
                },
                navigateToHome = {
                    navController.popUpToInclusiveAndNavigate(
                        SnapchatRoutes.Home.name,
                        SnapchatRoutes.Splash.name
                    )
                }
            )
        }

        composable(route = SnapchatRoutes.SignIn.name) {
            SignInScreen(
                navigateToSignUp = { navController.navigate(SnapchatRoutes.SignUp.name) },
                navigateToHome = {
                    navController.popUpToInclusiveAndNavigate(
                        SnapchatRoutes.Home.name,
                        SnapchatRoutes.SignIn.name
                    )
                }
            )
        }

        composable(route = SnapchatRoutes.SignUp.name) {
            SignUpScreen(
                navigateBack = { navController.popBackStack() },
                navigateToHome = {
                    navController.popUpToInclusiveAndNavigate(
                        SnapchatRoutes.Home.name,
                        SnapchatRoutes.SignIn.name
                    )
                }
            )
        }

        composable(route = SnapchatRoutes.Home.name) {
            HomeScreen(
                navigateToCamera = {
                    navController.navigate(SnapchatRoutes.Camera.name)
                },
                navigateToProfile = {
                    navController.navigate(SnapchatRoutes.Profile.name)
                }
            )
        }

        composable(route = SnapchatRoutes.Camera.name) {
            CameraScreen(
                navigateBack = { navController.popBackStack() },
                navigateToImagePreview = { navController.navigate(SnapchatRoutes.ImagePreview.name) }
            )
        }

        composable(
            route = SnapchatRoutes.ImagePreview.name
        ) {
            ImagePreviewScreen(
                navigateBack = { navController.popBackStack() },
                navigateToHome = { navController.clearBackStackAndNavigate(SnapchatRoutes.Home.name) }
            )
        }
        
        composable(route = SnapchatRoutes.Profile.name) {
            ProfileScreen(
                navigateBack = { navController.popBackStack() },
                navigateToSignIn = {
                    navController.clearBackStackAndNavigate(SnapchatRoutes.SignIn.name)
                }
            )
        }

    }
}

fun NavHostController.popUpToInclusiveAndNavigate(
    route: String,
    popUpRoute: String
) {
    this.navigate(route) {
        launchSingleTop = true
        popUpTo(popUpRoute) {
            inclusive = true
        }
    }
}

fun NavHostController.clearBackStackAndNavigate(
    route: String
) {
    this.navigate(route) {
        launchSingleTop = true
        popUpTo(this@clearBackStackAndNavigate.graph.id) {
            inclusive = true
        }
    }
}
