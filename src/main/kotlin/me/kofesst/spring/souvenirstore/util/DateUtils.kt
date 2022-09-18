package me.kofesst.spring.souvenirstore.util

import java.text.SimpleDateFormat
import java.util.*

val Date.calendar: Calendar
    get() = Calendar.getInstance().apply {
        time = this@calendar
    }

val Date.days: Int
    get() = this.calendar.get(Calendar.DAY_OF_MONTH)

val Date.months: Int
    get() = this.calendar.get(Calendar.MONTH)

val Date.years: Int
    get() = this.calendar.get(Calendar.YEAR)

infix fun Date.yearsUntil(other: Date): Int {
    var years = other.years - this.years
    if (other.months < this.months || (other.months == this.months && other.days < this.days)) {
        years -= 1
    }

    return years
}

fun Date.format(pattern: String): String {
    val sdf = SimpleDateFormat(pattern)
    return sdf.format(this)
}

fun Date.asTime() = this.format("HH:mm")