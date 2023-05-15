package com.example.snapchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.snapchat.ui.theme.SnapchatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SnapchatTheme {
                SnapchatApp()
            }
        }
    }
}
