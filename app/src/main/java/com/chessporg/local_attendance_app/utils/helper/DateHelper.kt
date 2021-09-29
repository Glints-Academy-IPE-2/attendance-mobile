package com.chessporg.local_attendance_app.utils.helper

object DateHelper {
    fun convertThreeLetterMonthToFullMonthName(threeLetterMonth: String): String {
        return when(threeLetterMonth.uppercase()) {
            "JAN" -> "January"
            "FEB" -> "February"
            "MAR" -> "March"
            "APR" -> "April"
            "MAY" -> "May"
            "JUN" -> "June"
            "JUL" -> "July"
            "AUG" -> "August"
            "SEP" -> "September"
            "OCT" -> "October"
            "NOV" -> "November"
            "DEC" -> "December"
            else -> "NONE"
        }
    }

    fun convertMonthToNumber(month: String): Int {
        return when(month) {
            "January" -> 1
            "February" -> 2
            "March" -> 3
            "April" -> 4
            "May" -> 5
            "June" -> 6
            "July" -> 7
            "August" -> 8
            "September" -> 9
            "October" -> 10
            "November" -> 11
            "December" -> 12
            else -> 0
        }
    }

    fun convertNumberToMonth(number: Int): String {
        return when(number) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "NONE"
        }
    }
}