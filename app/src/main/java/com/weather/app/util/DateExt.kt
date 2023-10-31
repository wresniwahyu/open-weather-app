package com.weather.app.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Long.formatDate(format: String = "EE, dd-MM-yyy"): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.toUtcFormatDate(format)
    } else {
        this.toUtcFormatDateOldVersion(format)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toUtcFormatDate(format: String): String {
    val instant = Instant.ofEpochMilli(this * 1000)
    val formatter = DateTimeFormatter.ofPattern(format).withZone(ZoneId.of("UTC"))
    return formatter.format(instant)
}

fun Long.toUtcFormatDateOldVersion(format: String): String {
    val timestampMillis = this * 1000 // Convert to milliseconds
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC") // Set the time zone to UTC
    return sdf.format(Date(timestampMillis))
}
