package com.chessporg.local_attendance_app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chessporg.local_attendance_app.api.RetrofitClient
import com.chessporg.local_attendance_app.data.model.Attendance
import com.chessporg.local_attendance_app.data.model.AttendanceListResponse
import com.chessporg.local_attendance_app.utils.DummyData
import com.chessporg.local_attendance_app.utils.helper.ResponseToModelHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class HomeViewModel : ViewModel() {

    fun getListAttendance(month: String, userId: Int): LiveData<ArrayList<Attendance>> {
        val attendanceList = MutableLiveData<ArrayList<Attendance>>()

        val data = HashMap<String, String>()
        data.put("month", month)
        data.put("userId", userId.toString())

        /* Fetching user attendances
        RetrofitClient
            .apiInstance
            .getAttendances(data)
            .enqueue(object : Callback<AttendanceListResponse> {
                override fun onResponse(
                    call: Call<AttendanceListResponse>,
                    response: Response<AttendanceListResponse>
                ) {
                    if (response.isSuccessful) {
                        val attendanceListResponse = response.body()?.attendances!!
                        attendanceListResponse.sortBy { it.userId }
                        val newList = ResponseToModelHelper
                            .mapAttendanceListResponseToAttendanceList(attendanceListResponse)
                        newList.removeLast()
                        attendanceList.postValue(newList)
                    }
                }

                override fun onFailure(call: Call<AttendanceListResponse>, t: Throwable) {
                    Timber.tag("Failure").d(t.message.toString())
                }
            })
         */

        val dummyAttendanceList = DummyData.getDummyAttendanceListResponse
        dummyAttendanceList.sortBy { it.userId }
        val newList = ResponseToModelHelper
            .mapAttendanceListResponseToAttendanceList(dummyAttendanceList)
        newList.removeLast()
        attendanceList.postValue(newList)

        return attendanceList
    }

    fun getTodayAttendance(month: String, userId: Int): LiveData<Attendance> {
        val todayAttendance = MutableLiveData<Attendance>()

        /* Fetching user today attendance
        RetrofitClient
            .apiInstance
            .getAttendances(month, userId)
            .enqueue(object : Callback<AttendanceListResponse> {
                override fun onResponse(
                    call: Call<AttendanceListResponse>,
                    response: Response<AttendanceListResponse>
                ) {
                    if (response.isSuccessful) {
                        val attendanceListResponse = response.body()?.attendances!!
                        attendanceListResponse.sortBy { it.date }
                        todayAttendance.postValue(
                            ResponseToModelHelper
                                .mapAttendanceResponseToAttendance(
                                    attendanceListResponse[attendanceListResponse.size-1]
                                )
                        )
                    }
                }

                override fun onFailure(call: Call<AttendanceListResponse>, t: Throwable) {

                }
            })
         */

        val dummyAttendanceList = DummyData.getDummyAttendanceListResponse
        dummyAttendanceList.sortBy { it.userId }
        val lastAttendance = dummyAttendanceList[dummyAttendanceList.size - 1].copy()

        todayAttendance.postValue(
            ResponseToModelHelper.mapAttendanceResponseToAttendance(lastAttendance)
        )

        return todayAttendance
    }
}