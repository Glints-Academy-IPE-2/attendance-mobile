package com.chessporg.local_attendance_app.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.chessporg.local_attendance_app.databinding.ActivityRegisterBinding
import com.chessporg.local_attendance_app.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonRegister.setOnClickListener {
            val email = binding.textFieldEmailRegist.editText?.text.toString().trim()
            val password = binding.textFieldPassRegist.editText?.text.toString().trim()
            val username = binding.textFieldUsernameRegist.editText?.text.toString().trim()

            if (validate(email, password, username)) {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        binding.textViewHere.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    fun isValidEmail(email: String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun validate(
        email: String,
        password: String,
        username: String
    ): Boolean {
        if (email.isEmpty()) {
            binding.textHelperEmailRegist.text = "Field tidak boleh kosong"
            binding.textHelperEmailRegist.visibility = View.VISIBLE
        } else if (!isValidEmail(email)) {
            binding.textHelperEmailRegist.text = "Masukkan email dengan benar"
        } else {
            binding.textHelperEmailRegist.visibility = View.GONE
        }

        if (password.isEmpty()) {
            binding.textHelperPassRegist.text = "Field tidak boleh kosong"
            binding.textHelperPassRegist.visibility = View.VISIBLE
        } else if (password.length < 8) {
            binding.textHelperPassRegist.text = "Password minimal 8 karakter"
            binding.textHelperPassRegist.visibility = View.VISIBLE
        } else {
            binding.textHelperPassRegist.visibility = View.GONE
        }

        if (username.isEmpty()) {
            binding.textHelperPassRegist.text = "Field tidak boleh kosong"
            binding.textHelperPassRegist.visibility = View.VISIBLE
        } else {
            binding.textHelperPassRegist.visibility = View.GONE
        }
        return true
    }
}