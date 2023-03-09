package com.example.pixsocialapplication.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtils {
    const val DATE_FORMATTER = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val DATE_CONVERT_FORMATTER = "a HH:mm"
    val TIME_ZONE = Locale.ITALIAN

    @SuppressLint("SimpleDateFormat")
    fun convertDate(data: String) : String {
        val utcFormatter = SimpleDateFormat(DATE_FORMATTER).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val utcDate = utcFormatter.parse(data)

        val krFormatter = SimpleDateFormat(DATE_CONVERT_FORMATTER).apply {
            timeZone = TimeZone.getTimeZone("Asia/Seoul")
        }

        return krFormatter.format(utcDate!!)
    }

    fun dateToString(data : String) : String {
        val formatter = SimpleDateFormat(DATE_FORMATTER, TIME_ZONE)
        var value = ""

        val newDateTime = formatter.parse(data)?.time

        val differenceValue =  currentDataToLong() - newDateTime!!

        when {
            differenceValue < 60000 -> { //59초 보다 적다면
                value = "방금 전"
            }
            differenceValue < 3600000 -> { //59분 보다 적다면
                value =  TimeUnit.MILLISECONDS.toMinutes(differenceValue).toString() + "분 전"
            }
            differenceValue < 86400000 -> { //23시간 보다 적다면
                value =  TimeUnit.MILLISECONDS.toHours(differenceValue).toString() + "시간 전"
            }
            differenceValue <  604800000 -> { //7일 보다 적다면
                value =  TimeUnit.MILLISECONDS.toDays(differenceValue).toString() + "일 전"
            }
            differenceValue < 2419200000 -> { //3주 보다 적다면
                value =  (TimeUnit.MILLISECONDS.toDays(differenceValue)/7).toString() + "주 전"
            }
            differenceValue < 31556952000 -> { //12개월 보다 적다면
                value =  (TimeUnit.MILLISECONDS.toDays(differenceValue)/30).toString() + "개월 전"
            }
            else -> { //그 외
                value =  (TimeUnit.MILLISECONDS.toDays(differenceValue)/365).toString() + "년 전"
            }
        }

        return value
    }

    fun currentDataToLong() : Long = Calendar.getInstance().timeInMillis
}