package com.chessporg.local_attendance_app.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chessporg.local_attendance_app.data.model.Attendance
import com.chessporg.local_attendance_app.databinding.ItemDailyAttendenceBinding

class DailyAttendanceAdapter : RecyclerView.Adapter<DailyAttendanceAdapter.CustomViewHolder>() {

    private val attendanceList = ArrayList<Attendance>()

    inner class CustomViewHolder(val binding: ItemDailyAttendenceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(attendance: Attendance) {
            binding.apply {
                val date = attendance.date.toString()

                val checkInTime = attendance.checkInTime.toString()
                val checkOutTime = attendance.checkOutTime.toString()
                tvDateDayText.text = date.subSequence(0, 3)
                tvDateDayNumber.text = date.subSequence(8, 10)
                tvStartHour.text = "${checkInTime.subSequence(11, 16)} WIB"
                tvEndHour.text = "${checkOutTime.subSequence(11, 16)} WIB"
            }
        }
    }


    fun setList(list: ArrayList<Attendance>) {
        attendanceList.clear()
        attendanceList.addAll(list)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        return CustomViewHolder(
            ItemDailyAttendenceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(attendanceList[position])
        Log.d("test attendance property", attendanceList[position].toString())
    }

    override fun getItemCount(): Int {
        return attendanceList.size
    }
}