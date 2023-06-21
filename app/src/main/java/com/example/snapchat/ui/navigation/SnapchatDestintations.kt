package com.example.snapchat.ui.navigation

sealed class Destinations(val route: String) {
    object RootGraph: Destinations("root_graph")
    object SplashScreen : Destinations("splash_screen")

    object AuthGraph : Destinations("auth_graph")
    object SignInScreen : Destinations("sign_in_screen")
    object SignUpScreen : Destinations("sign_up_screen")

    object MainGraph : Destinations("main_graph")
    object ProfileScreen : Destinations("profile_screen")
    object ChatScreen : Destinations("chat_screen")
    object SnapTakeScreen : Destinations("snap_take_screen")
    object SnapSendScreen : Destinations("snap_send_screen")
    object SnapPreviewScreen : Destinations("snap_preview_screen/{snapId}") {
        fun createRoute(snapId: String): String = "snap_preview_screen/$snapId"
    }
}