package com.example.snapchat.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.snapchat.R

@Composable
fun UsernameField(
    imeAction: ImeAction,
    keyboardActions: KeyboardActions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicField(
        placeholder = R.string.username,
        icon = R.drawable.ic_user,
        imeAction = imeAction,
        keyboardActions = keyboardActions,
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
    )
}

@Composable
fun EmailField(
    imeAction: ImeAction,
    keyboardActions: KeyboardActions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicField(
        placeholder = R.string.email,
        icon = R.drawable.ic_email,
        imeAction = imeAction,
        keyboardActions = keyboardActions,
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
    )
}

@Composable
fun PasswordField(
    imeAction: ImeAction,
    keyboardActions: KeyboardActions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PasswordField(
        R.string.password,
        imeAction,
        keyboardActions,
        value,
        onValueChange,
        modifier)
}

@Composable
fun RepeatPasswordField(
    imeAction: ImeAction,
    keyboardActions: KeyboardActions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PasswordField(
        R.string.repeat_password,
        imeAction,
        keyboardActions,
        value,
        onValueChange,
        modifier)
}

@Composable
fun MessageField(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var columnHeight by remember { mutableStateOf(0.dp) }

    OutlinedTextField(
        maxLines = 4,
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
            .onGloballyPositioned { coordinates ->
                columnHeight = with(density) { coordinates.size.height.toDp() }
            },
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(text = stringResource(R.string.add_a_caption)) },
        trailingIcon = {
            Column (
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.height(columnHeight)
            ) {
                BasicFilledIconButton(
                    icon = R.drawable.ic_send,
                    onClick = onSend,
                    modifier = Modifier.padding(4.dp)
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
private fun BasicField(
    @StringRes placeholder: Int,
    @DrawableRes icon: Int,
    imeAction: ImeAction,
    keyboardActions: KeyboardActions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp, 4.dp),
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(text = stringResource(placeholder)) },
        leadingIcon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = null
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions
    )
}

@Composable
private fun PasswordField(
    @StringRes placeholder: Int,
    imeAction: ImeAction,
    keyboardActions: KeyboardActions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    val icon =
        if (isVisible) painterResource(R.drawable.ic_visibility)
        else painterResource(R.drawable.ic_visibility_off)

    val visualTransformation =
        if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp, 4.dp),
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = { Text(text = stringResource(placeholder)) },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(
                onClick = { isVisible = !isVisible }
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation
    )
}