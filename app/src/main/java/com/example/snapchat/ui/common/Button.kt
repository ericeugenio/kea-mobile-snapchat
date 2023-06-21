package com.example.snapchat.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snapchat.R

@Composable
fun BasicTextButton(
    @StringRes text: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp)
    ) {
        Text(text = stringResource(text))
    }
}

@Composable
fun BasicIconButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
        )
    }
}

@Composable
fun BasicFilledIconButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    containerAlpha: Float = 1F
) {
    FilledTonalIconButton(
        onClick = onClick,
        modifier = modifier,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = containerAlpha),
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = iconModifier
        )
    }
}

@Composable
fun CameraLensIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_lens),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.onPrimary,
                        CircleShape
                    )
            )
        }
}

@Composable
fun BasicButton(
    @StringRes text: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = stringResource(text),
            fontSize = 16.sp
        )
    }
}

@Composable
fun BasicTonalButton(
    @StringRes text: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp),
    ) {
        Text(
            text = stringResource(text),
            fontSize = 16.sp
        )
    }
}