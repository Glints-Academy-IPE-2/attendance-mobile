package com.chessporg.local_attendance_app.utils

import com.chessporg.local_attendance_app.data.model.AttendanceResponse
import com.chessporg.local_attendance_app.data.model.AttendanceTime
import java.util.*

object DummyData {
    val userId: Int = 1

    val getDummyAttendanceListResponse: ArrayList<AttendanceResponse>
        get() {
            val date = Date()
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DATE, -8)

            val date9 = calendar.time
            calendar.add(Calendar.DATE, 1)

            val date8 = calendar.time
            calendar.add(Calendar.DATE, 1)

            val date7 = calendar.time
            calendar.add(Calendar.DATE, 1)

            val date6 = calendar.time
            calendar.add(Calendar.DATE, 1)

            val date5 = calendar.time
            calendar.add(Calendar.DATE, 1)

            val date4 = calendar.time
            calendar.add(Calendar.DATE, 1)

            val date3 = calendar.time
            calendar.add(Calendar.DATE, 1)

            val date2 = calendar.time
            calendar.add(Calendar.DATE, 1)

            val date1 = calendar.time
            calendar.add(Calendar.DATE, 1)

            return arrayListOf(
                AttendanceResponse(
                    userId = 1,
                    date = date9,
                    time = AttendanceTime(
                        checkIn = date9,
                        checkOut = date9,
                    )
                ),
                AttendanceResponse(
                    userId = 1,
                    date = date8,
                    time = AttendanceTime(
                        checkIn = date8,
                        checkOut = date8,
                    )
                ),
                AttendanceResponse(
                    userId = 1,
                    date = date7,
                    time = AttendanceTime(
                        checkIn = date7,
                        checkOut = date7,
                    )
                ),
                AttendanceResponse(
                    userId = 1,
                    date = date6,
                    time = AttendanceTime(
                        checkIn = date6,
                        checkOut = date6,
                    )
                ),
                AttendanceResponse(
                    userId = 1,
                    date = date5,
                    time = AttendanceTime(
                        checkIn = date5,
                        checkOut = date5,
                    )
                ),
                AttendanceResponse(
                    userId = 1,
                    date = date4,
                    time = AttendanceTime(
                        checkIn = date4,
                        checkOut = date4,
                    )
                ),
                AttendanceResponse(
                    userId = 1,
                    date = date3,
                    time = AttendanceTime(
                        checkIn = date3,
                        checkOut = date3,
                    )
                ),
                AttendanceResponse(
                    userId = 1,
                    date = date2,
                    time = AttendanceTime(
                        checkIn = date2,
                        checkOut = date2,
                    )
                ),
                AttendanceResponse(
                    userId = 1,
                    date = date1,
                    time = AttendanceTime(
                        checkIn = date1,
                        checkOut = date1,
                    )
                )
            )
        }
}