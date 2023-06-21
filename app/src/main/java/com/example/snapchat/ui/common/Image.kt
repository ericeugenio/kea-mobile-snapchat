package com.example.snapchat.ui.common

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.snapchat.R

@Composable
fun ImageView(
    image: Uri?,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier,
    ) {
        AsyncImage(
            model = image,
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxSize()
        )

        BasicIconButton(
            icon = R.drawable.ic_close,
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )

        content()
    }
}