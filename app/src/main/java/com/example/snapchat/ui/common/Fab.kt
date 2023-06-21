package com.example.snapchat.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

data class MultiFabItemData(
    val label: Int,
    val icon: Int,
    val onCLick: () -> Unit,
)

@Composable
fun MultiFab(
    @DrawableRes icon: Int,
    items: List<MultiFabItemData>,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isVisible) 315F else 0F,
        animationSpec = tween(1000)
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.End
    ) {
        AnimatedVisibility(visible = isVisible) {
            Column(
                modifier = modifier.padding(end = 4.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.End
            ) {
                items.forEach {
                    MultiFabItem(
                        label = it.label,
                        icon = it.icon,
                        onClick = it.onCLick
                    )
                }
            }
        }
        FloatingActionButton(onClick = { isVisible = !isVisible }) {
            Icon(
                modifier = Modifier.rotate(rotationAngle),
                painter = painterResource(id = icon),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun MultiFabItem(
    @StringRes label: Int,
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface (
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Text(
                text = stringResource(id = label),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        SmallFloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null
            )
        }
    }
}