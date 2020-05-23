package dev.kingominho.covid_19dashboard.util

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*
import java.util.concurrent.TimeUnit

@SuppressLint("SimpleDateFormat")
fun getPeriod(past: Date): String {
    val now = Date()
    val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
    val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)

    return when {
        seconds < 60 -> {
            "Few seconds ago"
        }
        minutes < 60 -> {
            "$minutes minutes ago"
        }
        hours < 24 -> {
            "$hours hour ${minutes % 60} min ago"
        }
        else -> {
            SimpleDateFormat("dd-MM-yyyy hh:mm a").format(past).toString()
        }
    }
}

/**
 * Parses String to "dd/MM/yyyy HH:mm:ss" date and time format.
 */
@SuppressLint("SimpleDateFormat", "NewApi")
fun String.toDateFormat(): Date {
    val ta: TemporalAccessor = DateTimeFormatter.ISO_INSTANT.parse(this)
    val i: Instant = Instant.from(ta)
    return Date.from(i)
}

@SuppressLint("NewApi")
fun String.convertDateFormat(): String {
    val ta: TemporalAccessor = DateTimeFormatter.ISO_INSTANT.parse(this)
    val i: Instant = Instant.from(ta)
    val d = Date.from(i)
    Log.d("Tag", "Given: $this and converted: ${d}")
    return d.toString()
}