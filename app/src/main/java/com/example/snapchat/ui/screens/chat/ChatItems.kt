package com.example.snapchat.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snapchat.R
import com.example.snapchat.ui.theme.SnapchatTheme

@Composable
fun SnapHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6F),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal= 12.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun SentSnapItem(
    sentAt: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    SnapItem(
        sentAt = sentAt,
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_send),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(
                text = stringResource(id = R.string.snap_sent)
            )
        }
    }
}

@Composable
fun ReceivedSnapItem(
    sentAt: String,
    senderName: String,
    isViewed: Boolean,
    modifier: Modifier = Modifier,
    showSenderName: Boolean = true,
    onClick: () -> Unit = {}
) {
    val spacerBgColor =
        if (isViewed) Color.Transparent
        else MaterialTheme.colorScheme.primary

    val text =
        if (isViewed) R.string.snap_viewed
        else R.string.snap_tap_to_open

    SnapItem(
        sentAt = sentAt,
        onClick = { if (!isViewed) onClick() else Unit },
        modifier = modifier
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            if (showSenderName) {
                Text(
                    text = senderName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(24.dp)
                        .background(
                            color = spacerBgColor,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(4.dp)
                        )
                )
                Text(
                    text = stringResource(id = text)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SnapItem(
    sentAt: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedCard(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Box (
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                content()
                Text(
                    text = sentAt,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SentMessageItemPreview()
{
    SnapchatTheme {
        SentSnapItem(
            sentAt = "21:50",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ViewedReceivedMessageItemPreview()
{
    SnapchatTheme {
        Column (
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SnapHeader(title = "Today")
            ReceivedSnapItem(
                sentAt = "21:50",
                senderName = "username",
                isViewed = true
            )
            ReceivedSnapItem(
                sentAt = "21:50",
                senderName = "username",
                isViewed = true,
                showSenderName = false
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun NonViewedReceivedMessageItemPreview()
{
    SnapchatTheme {
        ReceivedSnapItem(
            senderName = "username",
            sentAt = "21:50",
            isViewed = false
        )
    }
}

