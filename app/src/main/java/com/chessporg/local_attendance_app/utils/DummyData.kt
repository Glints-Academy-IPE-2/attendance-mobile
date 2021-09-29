package com.chessporg.local_attendance_app.utils

import com.chessporg.local_attendance_app.data.model.AttendanceResponse
import com.chessporg.local_attendance_app.data.model.AttendanceTime
import java.util.*

object DummyData {
    val userId: Int = 1

    val getDummyAttendanceListResponse: ArrayList<AttendanceResponse>
        get() {
            return arrayListOf(
                AttendanceResponse(
                    userId = 1,
                    date = Date(),
                    time = AttendanceTime(
                        checkIn = Date(),
                        checkOut = Date(),
                    )
                ),
                AttendanceResponse(
                    userId = 2,
                    date = Date(),
                    time = AttendanceTime(
                        checkIn = Date(),
                        checkOut = Date(),
                    )
                ),
                AttendanceResponse(
                    userId = 3,
                    date = Date(),
                    time = AttendanceTime(
                        checkIn = Date(),
                        checkOut = Date(),
                    )
                ),
                AttendanceResponse(
                    userId = 4,
                    date = Date(),
                    time = AttendanceTime(
                        checkIn = Date(),
                        checkOut = Date(),
                    )
                ),
                AttendanceResponse(
                    userId = 5,
                    date = Date(),
                    time = AttendanceTime(
                        checkIn = Date(),
                        checkOut = Date(),
                    )
                ),
                AttendanceResponse(
                    userId = 6,
                    date = Date(),
                    time = AttendanceTime(
                        checkIn = Date(),
                        checkOut = Date(),
                    )
                ),
                AttendanceResponse(
                    userId = 41,
                    date = Date(),
                    time = AttendanceTime(
                        checkIn = Date(),
                        checkOut = Date(),
                    )
                ),
                AttendanceResponse(
                    userId = 22,
                    date = Date(),
                    time = AttendanceTime(
                        checkIn = Date(),
                        checkOut = Date(),
                    )
                ),
                AttendanceResponse(
                    userId = 101,
                    date = Date(),
                    time = AttendanceTime(
                        checkIn = Date(),
                        checkOut = Date(),
                    )
                )
            )
        }
}