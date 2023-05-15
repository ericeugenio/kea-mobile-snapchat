package com.example.snapchat.ui.common

import android.Manifest
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.snapchat.R

@Composable
fun PermissionDialog(
    @StringRes title: Int,
    @StringRes text: Int,
    @DrawableRes icon: Int,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        title = { Text(text = stringResource(id = title)) },
        text = { Text(text = stringResource(id = text)) },
        icon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Dialog icon"
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Got it")
            }
        },
        onDismissRequest = {},
    )
}

object PermissionDialogContentProvider {
    fun getTitle(permission: String): Int {
        return when (permission) {
            Manifest.permission.CAMERA -> R.string.permission_camera_title
            else -> return -1
        }
    }

    fun getText(permission: String, isPermanentlyDenied: Boolean): Int {
        return when (permission) {
            Manifest.permission.CAMERA -> {
                if (isPermanentlyDenied) R.string.permission_camera_permanently_denied_text
                else R.string.permission_camera_rationale_text
            }
            else -> return -1
        }
    }

    fun getIcon(permission: String): Int {
        return when (permission) {
            Manifest.permission.CAMERA -> R.drawable.ic_camera
            else -> return -1
        }
    }
}

