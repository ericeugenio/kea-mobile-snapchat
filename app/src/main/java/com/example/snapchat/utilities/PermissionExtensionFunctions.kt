package com.example.snapchat.utilities

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun Context.getActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }

        context = context.baseContext
    }

    throw java.lang.IllegalStateException()
}

fun Context.checkPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED
}

fun Activity.shouldShowRationale(permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
}

fun Activity.navigateToSettings() {
    startActivity(Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:$packageName")
    ))
}