package com.example.snapchat.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snapchat.R
import com.example.snapchat.ui.ModelViewProvider
import com.example.snapchat.ui.common.*
import com.example.snapchat.ui.theme.SnapchatTheme
import com.example.snapchat.utilities.checkPermission
import com.example.snapchat.utilities.getActivity
import com.example.snapchat.utilities.shouldShowRationale

@Composable
fun HomeScreen (
    navigateToCamera: () -> Unit,
    navigateToProfile: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = ModelViewProvider.Factory)
) {
    val permissionsQueueState by viewModel.permissionsQueueState.collectAsState()

    val context = LocalContext.current
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissions.forEach { (permission, isGranted) ->
                viewModel.onPermissionResult(
                    permission = permission,
                    isGranted = isGranted,
                    onGranted = navigateToCamera
                )
            }
        }
    )

    val fabItems = listOf(
        MultiFabItemData(label = R.string.image, icon = R.drawable.ic_image) {

        },
        MultiFabItemData(label = R.string.camera, icon = R.drawable.ic_camera) {
            if (context.checkPermission(Manifest.permission.CAMERA)) navigateToCamera()
            else permissionsLauncher.launch(arrayOf(Manifest.permission.CAMERA))
        }
    )

    Scaffold (
        modifier = modifier,
        topBar = {
            ActionTopAppBar(
                title = R.string.app_name,
                actionIcon = R.drawable.ic_user,
                onAction = { navigateToProfile() }
            )
        },
        floatingActionButton =  {
            MultiFab(icon = R.drawable.ic_send, items = fabItems)
        }
    ) { contentPadding ->
        HomeBody(
            modifier = modifier.padding(contentPadding)
        )

        permissionsQueueState
            .reversed()
            .forEach { permission ->
                val isPermanentlyDenied = !context.getActivity().shouldShowRationale(permission)

                PermissionDialog(
                    onConfirm = {
                        viewModel.onPermissionDialogDismiss()

                        if (isPermanentlyDenied) navigateToCamera()
                        else permissionsLauncher.launch(arrayOf(Manifest.permission.CAMERA))
                    },
                    title = PermissionDialogContentProvider.getTitle(permission),
                    text = PermissionDialogContentProvider.getText(permission, isPermanentlyDenied),
                    icon = PermissionDialogContentProvider.getIcon(permission)
                )
            }
    }
}

@Composable
fun HomeBody(
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "HOME")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview()
{
    SnapchatTheme {
        HomeBody()
    }
}