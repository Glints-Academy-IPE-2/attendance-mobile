package com.chessporg.local_attendance_app.utils.helper

import com.chessporg.local_attendance_app.data.model.Attendance
import com.chessporg.local_attendance_app.data.model.AttendanceResponse

object ResponseToModelHelper {
    fun mapAttendanceListResponseToAttendanceList(list: ArrayList<AttendanceResponse>)
        : ArrayList<Attendance>
    {
        val newList = ArrayList<Attendance>()
        for(response in list) {
            newList.add(mapAttendanceResponseToAttendance(response))
        }
        return newList
    }

    fun mapAttendanceResponseToAttendance(attendanceResponse: AttendanceResponse)
        : Attendance
    {
        return Attendance(
            userId = attendanceResponse.userId,
            date = attendanceResponse.date,
            checkInTime = attendanceResponse.time.checkIn,
            checkOutTime = attendanceResponse.time.checkOut,
        )
    }
}