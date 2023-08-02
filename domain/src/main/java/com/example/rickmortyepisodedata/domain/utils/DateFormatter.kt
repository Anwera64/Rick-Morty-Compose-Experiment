package com.example.rickmortyepisodedata.domain.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {

    const val FORMAT_2 = "MMMM d, yyyy"
    const val FORMAT_1 = "dd MMMM yyyy"

    fun format(date: Date?, format: String): String? {
        if (date == null) {
            return null
        }
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(date)
    }

    fun parse(date: String?, format: String): Date? {
        if (date == null) {
            return null
        }
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.parse(date)
    }
}