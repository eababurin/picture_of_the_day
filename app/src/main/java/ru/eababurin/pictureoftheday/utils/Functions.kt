package ru.eababurin.pictureoftheday.utils

import java.text.SimpleDateFormat
import java.util.*

internal fun requireDate(offset: Int): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, offset)
    return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time).toString()
}