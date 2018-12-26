package com.oklb2018.frailtypreventionsupportsystem.elements

import java.util.*
import kotlin.collections.ArrayList

class AppManager {
    companion object {
        public val firstDay = Calendar.getInstance().apply {
            set(2018, 10, 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

        public val dateAndTimeToday = Calendar.getInstance()

        public val dateToday = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        public fun compareByDate(c1: Calendar, c2: Calendar): Int {
            val result = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR)
            return if (result != 0) result else c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR)
        }

        public fun generateCalendars(): ArrayList<Calendar> {
            val dates = ArrayList<Calendar>()
            val d = (dateToday.time.time - firstDay.time.time) / (1000 * 60 * 60 * 24)
            for (i in 0 until d.toInt()) {
                val c = Calendar.getInstance()
                c.time = firstDay.time
                c.add(Calendar.DAY_OF_MONTH, i)
                dates.add(c)
            }
            dates.add(dateToday)
            return dates
        }

        public fun compareDate(c1: Calendar, c2: Calendar): Boolean {
            return c1.time.time == c2.time.time
        }

        public fun deffDate(c1: Calendar, c2: Calendar = firstDay): Int{
            return ((c1.time.time - c2.time.time) / (1000 * 60 * 60 * 24)).toInt()
        }
    }
}