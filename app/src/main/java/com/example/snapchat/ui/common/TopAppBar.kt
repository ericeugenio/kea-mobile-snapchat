package com.example.snapchat.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationTopAppBar(
    @StringRes title: Int,
    @DrawableRes navigationIcon: Int,
    onNavigation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(title).replaceFirstChar { it.uppercaseChar() },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier,
        colors = topAppBarColors(),
        navigationIcon = {
            IconButton(onClick = onNavigation) {
                Icon(
                    painter = painterResource(navigationIcon),
                    contentDescription = "TopAppBar Navigation"
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionTopAppBar(
    @StringRes title: Int,
    @DrawableRes actionIcon: Int,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(title).replaceFirstChar { it.uppercaseChar() },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier,
        colors = topAppBarColors(),
        actions = {
            IconButton(onClick = onAction) {
                Icon(
                    painter = painterResource(actionIcon),
                    contentDescription = "TopAppBar Action"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun topAppBarColors(): TopAppBarColors {
    return TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
    )
}