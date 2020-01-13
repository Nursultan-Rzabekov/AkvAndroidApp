package com.example.akvandroidapp.util

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs

class Converters {
    companion object {
        fun humanReadableByteCountSI(bytes: Long): String {
            val s = if (bytes < 0) "-" else ""
            var b = if (bytes == Long.MIN_VALUE) Long.MAX_VALUE else abs(bytes)
            if (b < 1000L)
                return "$bytes B"
            else if (b < 999_950L)
                return "${BigDecimal(b/1e3).setScale(1, RoundingMode.HALF_EVEN)} KB"
            else if ((b / 1000) < 999_950L) {
                b /= 1000
                return "${BigDecimal(b/1e3).setScale(1, RoundingMode.HALF_EVEN)} MB"
            } else if ((b / 1000) < 999_950L) {
                b /= 1000
                return "${BigDecimal(b/1e3).setScale(1, RoundingMode.HALF_EVEN)} GB"
            } else if ((b / 1000) < 999_950L) {
                b /= 1000
                return "${BigDecimal(b/1e3).setScale(1, RoundingMode.HALF_EVEN)} TB"
            } else if ((b / 1000) < 999_950L) {
                b /= 1000
                return "${BigDecimal(b/1e3).setScale(1, RoundingMode.HALF_EVEN)} PB"
            } else return "${BigDecimal(b/1e3).setScale(1, RoundingMode.HALF_EVEN)} PB"
        }
    }
}