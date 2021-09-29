package com.chessporg.local_attendance_app.ui.locationview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chessporg.local_attendance_app.data.model.AttendanceResponse
import com.chessporg.local_attendance_app.utils.DummyData

class LocationViewViewModel : ViewModel() {

    fun getTodayAttendance(month: String, userId: Int): LiveData<AttendanceResponse> {
        val todayAttendance = MutableLiveData<AttendanceResponse>()

        /*
        RetrofitClient
            .apiInstance
            .getAttendances(month, userId)
            .enqueue(object : Callback<AttendanceListResponse> {
                override fun onResponse(
                    call: Call<AttendanceListResponse>,
                    response: Response<AttendanceListResponse>
                ) {
                    if (response.isSuccessful) {
                        val listAttedance = response.body()?.attendances!!
                        val lastAttendance = listAttedance[listAttedance.size-1]
                        todayAttendance.postValue(lastAttendance)
                    }
                }
                override fun onFailure(call: Call<AttendanceListResponse>, t: Throwable) {

                }
            })
         */

        val listAttendance = DummyData.getDummyAttendanceListResponse
        val lastAttendance = listAttendance[listAttendance.size-1]
        todayAttendance.postValue(lastAttendance)

        return todayAttendance
    }

    fun checkOutTodayAttendance(month: String, userId: Int) {

    }
}