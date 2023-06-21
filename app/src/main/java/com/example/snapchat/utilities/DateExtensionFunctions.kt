package com.example.snapchat.utilities

import android.annotation.SuppressLint
import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
fun Timestamp.toLocalDate(): LocalDate = toDate()
    .toInstant()
    .atZone(ZoneId.systemDefault())
    .toLocalDate()

@SuppressLint("NewApi")
fun Timestamp.toStringPattern(pattern: String): String = toDate()
    .toInstant()
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()
    .format(DateTimeFormatter.ofPattern(pattern))


@SuppressLint("NewApi")
fun LocalDate.toMessageHeaderTitle(): String {
    val currentDate = LocalDate.now()

    val isOneWeekOld = currentDate.minusWeeks(1).plusDays(1).isAfter(this)
    val isToday = currentDate.isEqual(this)
    val isYesterday = currentDate.minusDays(1).isEqual(this)

    return if (isOneWeekOld) {
        this.format(DateTimeFormatter.ofPattern("E, dd MMM"))
    }
    else {
        if (isToday) "Today"
        else if (isYesterday) "Yesterday"
        else this.format(DateTimeFormatter.ofPattern("EEEE"))
    }
}
