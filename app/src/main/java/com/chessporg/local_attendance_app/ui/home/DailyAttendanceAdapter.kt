package com.chessporg.local_attendance_app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chessporg.local_attendance_app.databinding.ItemDailyAttendenceBinding

class DailyAttendanceAdapter : RecyclerView.Adapter<DailyAttendanceAdapter.CustomViewHolder>() {
    inner class CustomViewHolder(val binding: ItemDailyAttendenceBinding) : RecyclerView.ViewHolder(binding.root)  {
        fun bind() {

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        return CustomViewHolder(ItemDailyAttendenceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return 10
    }
}