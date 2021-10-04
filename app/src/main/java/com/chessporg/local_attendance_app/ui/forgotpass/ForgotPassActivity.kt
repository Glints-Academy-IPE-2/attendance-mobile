package com.chessporg.local_attendance_app.ui.forgotpass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isEmpty
import com.chessporg.local_attendance_app.databinding.ActivityForgotPassBinding
import com.chessporg.local_attendance_app.ui.login.LoginActivity

class ForgotPassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPassBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //set logic forgot password sisa tambah database
        binding.buttonForgotPass.setOnClickListener {
            if (binding.textFieldEmailForgot.isEmpty()) {
                binding.textHelperEmailReset.setText("Field tidak boleh kosong")
                binding.textHelperEmailReset.visibility = View.VISIBLE
            } else {
                binding.textHelperEmailReset.visibility = View.GONE
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }
}