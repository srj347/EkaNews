package com.example.core.ui.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.core.ui.localization.Localization
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object Utils {
    fun shareContent(context: Context, contentUrl: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, contentUrl)
        }
        context.startActivity(Intent.createChooser(shareIntent, Localization.SHARE_ARTICLE_VIA))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(inputDate: String): String {
        try {
            val inputDateTime = ZonedDateTime.parse(inputDate)
            val currentDateTime = ZonedDateTime.now(ZoneOffset.UTC)
            val currentDate = currentDateTime.toLocalDate()
            val inputDateLocal = inputDateTime.toLocalDate()

            if (inputDateLocal == currentDate) {
                val minutesAgo = ChronoUnit.MINUTES.between(inputDateTime, currentDateTime)
                return when {
                    minutesAgo < 1 -> "Just now"
                    minutesAgo < 60 -> "$minutesAgo minute${if (minutesAgo > 1) "s" else ""} ago"
                    minutesAgo < 120 -> "1 hour ago"
                    else -> {
                        val hoursAgo = ChronoUnit.HOURS.between(inputDateTime, currentDateTime)
                        "$hoursAgo hour${if (hoursAgo > 1) "s" else ""} ago"
                    }
                }
            }

            val yesterday = currentDate.minusDays(1)
            if (inputDateLocal == yesterday) {
                return "Yesterday"
            }

            val formatter = DateTimeFormatter.ofPattern("d MMM yyyy")
            return inputDateTime.format(formatter)
        } catch (e: Exception) {
            return "Unknown Date"
        }
    }
}