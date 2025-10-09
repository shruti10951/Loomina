package com.shrujan.loomina.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(isoDate: String?) : String {
        if (isoDate.isNullOrEmpty()) return ""

        return try {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            val dateTime = LocalDateTime.parse(isoDate, inputFormatter)

            val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
            dateTime.format(outputFormatter)
        } catch (e: Exception) {
            Log.d("DateUtils", "formatDate error: ${e.message}")
            ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRelativeTime(isoDate: String?) : String {
        if (isoDate.isNullOrEmpty()) return ""
        return try {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            val postedTime = LocalDateTime.parse(isoDate, inputFormatter)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()

            val now = LocalDateTime.now()
            val duration = Duration.between(postedTime, now)

            when {
                duration.toMinutes() < 1 -> "just now"
                duration.toMinutes() < 60 -> "${duration.toMinutes()} min ago"
                duration.toHours() < 24 -> "${duration.toHours()} hr ago"
                duration.toDays() < 7 -> "${duration.toDays()} days ago"
                duration.toDays() < 30 -> "${duration.toDays() / 7} weeks ago"
                else -> formatDate(isoDate)
            }
        } catch (e: Exception) {
            Log.d("DateUtils", "getRelativeTime error: ${e.message}")
            ""
        }
    }
}
