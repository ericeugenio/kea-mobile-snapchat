package com.example.snapchat.ui.screens.chat

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snapchat.R
import com.example.snapchat.data.model.Snap
import com.example.snapchat.data.model.User
import com.example.snapchat.ui.ModelViewProvider
import com.example.snapchat.ui.common.*
import com.example.snapchat.ui.theme.SnapchatTheme
import com.example.snapchat.utilities.checkPermission
import com.example.snapchat.utilities.getActivity
import com.example.snapchat.utilities.shouldShowRationale
import com.example.snapchat.utilities.toMessageHeaderTitle
import com.example.snapchat.utilities.toStringPattern
import com.google.firebase.Timestamp
import java.time.LocalDate
import java.util.SortedMap

@SuppressLint("InlinedApi")
@Composable
fun ChatScreen (
    navigateToSnapTake: () -> Unit,
    navigateToSnapSend: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSnapPreview: (snapId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = viewModel(factory = ModelViewProvider.Factory)
) {
    val permissionsQueueState by viewModel.permissionsQueueState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissions.forEach { (permission, isGranted) ->
                viewModel.onPermissionResult(
                    permission = permission,
                    isGranted = isGranted,
                    onGranted = navigateToSnapTake
                )
            }
        }
    )
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.onImageSelected(uri, navigateToSnapSend)
            }
        }
    )

    // This is another way to take a picture from the camera app instead of implementing
    // own camera.
    /*
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
        }
    )
    */

    val fabItems = listOf(
        MultiFabItemData(label = R.string.image, icon = R.drawable.ic_image) {
            if (context.checkPermission(Manifest.permission.ACCESS_MEDIA_LOCATION))
                galleryLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
            else
                permissionsLauncher.launch(arrayOf(Manifest.permission.ACCESS_MEDIA_LOCATION))
        },
        MultiFabItemData(label = R.string.camera, icon = R.drawable.ic_camera) {
            if (context.checkPermission(Manifest.permission.CAMERA)) navigateToSnapTake()
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
        ChatBody(
            groupedSnaps = uiState.snaps,
            onReceivedSnapTap = navigateToSnapPreview,
            modifier = modifier.padding(contentPadding)
        )

        permissionsQueueState.permissionsQueue
            .reversed()
            .forEach { permission ->
                val isPermanentlyDenied = !context.getActivity().shouldShowRationale(permission)

                PermissionDialog(
                    onConfirm = {
                        viewModel.onPermissionDialogDismiss()

                        if (isPermanentlyDenied) {
                            when (permission) {
                                Manifest.permission.CAMERA -> navigateToSnapTake()
                                Manifest.permission.ACCESS_MEDIA_LOCATION -> Unit
                            }
                        }
                        else permissionsLauncher.launch(arrayOf(permission))
                    },
                    title = PermissionDialogContentProvider.getTitle(permission),
                    text = PermissionDialogContentProvider.getText(permission, isPermanentlyDenied),
                    icon = PermissionDialogContentProvider.getIcon(permission)
                )
            }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatBody(
    groupedSnaps: SortedMap<LocalDate, List<Snap>>,
    onReceivedSnapTap: (snapId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn (
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    ) {
        groupedSnaps.forEach { (key, snaps) ->
            var prevSenderId = ""

            stickyHeader {
                SnapHeader(
                    title = key.toMessageHeaderTitle(),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            items(
                items = snaps,
                key = { it.id }
            ) { snap ->
                when (snap) {
                    is Snap.Sent -> SentSnapItem(
                        sentAt = snap.sentAt.toStringPattern("HH:mm")
                    )
                    is Snap.Received -> ReceivedSnapItem(
                        sentAt =  snap.sentAt.toStringPattern("HH:mm"),
                        senderName = snap.sender.username,
                        isViewed = snap.isViewed,
                        showSenderName = prevSenderId != snap.sender.id,
                        onClick = { onReceivedSnapTap(snap.id) }
                    )
                    else -> Unit
                }

                prevSenderId = if (snap is Snap.Received) snap.sender.id else ""
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview()
{
    val mockSnaps = mutableListOf(
        Snap.Sent("message1", Timestamp.now()),
        Snap.Sent("message2", Timestamp.now()),
        Snap.Received("message3", User(id = "sender1", username = "sender1"), true, Timestamp.now()),
        Snap.Received("message4", User(id = "sender1", username = "sender1"), false, Timestamp.now()),
        Snap.Received("message5", User(id = "sender2", username = "sender2"), false, Timestamp.now()),
        Snap.Sent("message6", Timestamp.now()),
    )
    .groupBy { it.getKey() }
    .toSortedMap()

    SnapchatTheme {
        ChatBody(
            groupedSnaps = mockSnaps,
            onReceivedSnapTap = {}
        )
    }
}