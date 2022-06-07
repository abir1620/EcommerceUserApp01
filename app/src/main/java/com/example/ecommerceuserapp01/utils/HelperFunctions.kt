package com.example.ecommerceuserapp01.utils

import java.text.SimpleDateFormat
import java.util.*

fun getFormattedDate(date: Long, pattern: String) =
    SimpleDateFormat(pattern).format(Date(date))
