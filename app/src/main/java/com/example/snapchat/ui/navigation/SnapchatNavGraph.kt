package com.example.snapchat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.snapchat.ui.screens.chat.ChatScreen
import com.example.snapchat.ui.screens.auth.SignInScreen
import com.example.snapchat.ui.screens.auth.SignUpScreen
import com.example.snapchat.ui.screens.chat.snap.SnapTakeScreen
import com.example.snapchat.ui.screens.chat.snap.SnapSendScreen
import com.example.snapchat.ui.screens.auth.ProfileScreen
import com.example.snapchat.ui.screens.chat.snap.SnapPreviewScreen
import com.example.snapchat.ui.screens.splash.SplashScreen
import com.example.snapchat.utilities.popUpToAndNavigate

@Composable
fun SnapchatNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.SplashScreen.route,
        route = Destinations.RootGraph.route,
        modifier = modifier
    ) {
        composable(route = Destinations.SplashScreen.route) {
            SplashScreen(
                navigateToSignIn = {
                    navController.popUpToAndNavigate(
                        Destinations.AuthGraph.route,
                        Destinations.RootGraph.route
                    )
                },
                navigateToChat = {
                    navController.popUpToAndNavigate(
                        Destinations.MainGraph.route,
                        Destinations.RootGraph.route
                    )
                }
            )
        }

        authGraph(navController)
        mainGraph(navController)
    }
}

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        startDestination = Destinations.SignInScreen.route,
        route = Destinations.AuthGraph.route
    ) {
        composable(route = Destinations.SignInScreen.route) {
            SignInScreen(
                navigateToSignUp = { navController.navigate(Destinations.SignUpScreen.route) },
                navigateToChat = {
                    navController.popUpToAndNavigate(
                        Destinations.MainGraph.route,
                        Destinations.AuthGraph.route
                    )
                }
            )
        }

        composable(route = Destinations.SignUpScreen.route) {
            SignUpScreen(
                navigateBack = { navController.popBackStack() },
                navigateToChat = {
                    navController.popUpToAndNavigate(
                        Destinations.MainGraph.route,
                        Destinations.AuthGraph.route
                    )
                }
            )
        }
    }
}

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation(
        startDestination = Destinations.ChatScreen.route,
        route = Destinations.MainGraph.route
    ) {
        composable(route = Destinations.ChatScreen.route) {
            ChatScreen(
                navigateToProfile = { navController.navigate(Destinations.ProfileScreen.route) },
                navigateToSnapTake = { navController.navigate(Destinations.SnapTakeScreen.route) },
                navigateToSnapSend = { navController.navigate(Destinations.SnapSendScreen.route) },
                navigateToSnapPreview = { snapId ->
                    navController.navigate(Destinations.SnapPreviewScreen.createRoute(snapId))
                }
            )
        }

        composable(route = Destinations.ProfileScreen.route) {
            ProfileScreen(
                navigateBack = { navController.popBackStack() },
                navigateToSignIn = {
                    navController.popUpToAndNavigate(
                        Destinations.AuthGraph.route,
                        Destinations.MainGraph.route
                    )
                }
            )
        }

        composable(route = Destinations.SnapTakeScreen.route) {
            SnapTakeScreen(
                navigateBack = { navController.popBackStack() },
                navigateToSnapSend = { navController.navigate(Destinations.SnapSendScreen.route) }
            )
        }

        composable(
            route = Destinations.SnapSendScreen.route
        ) {
            SnapSendScreen(
                navigateBack = { navController.popBackStack() },
                navigateToChat = {
                    navController.popUpToAndNavigate(
                        Destinations.ChatScreen.route,
                        Destinations.MainGraph.route
                    )
                }
            )
        }

        composable(
            route = Destinations.SnapPreviewScreen.route,
            arguments = listOf(
                navArgument("snapId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            SnapPreviewScreen(
                snapId = backStackEntry.arguments?.getString("snapId"),
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
