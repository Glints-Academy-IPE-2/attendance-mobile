package com.chessporg.local_attendance_app.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import com.chessporg.local_attendance_app.databinding.ActivityLoginBinding
import com.chessporg.local_attendance_app.ui.blank.BlankActivity
import com.chessporg.local_attendance_app.ui.forgotpass.ForgotPassActivity
import com.chessporg.local_attendance_app.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.textViewForgot.setOnClickListener {
            startActivity(Intent(this, ForgotPassActivity::class.java))
        }

        //set logic logic sisa tambah database
        binding.buttonLogin.setOnClickListener {
            val email = binding.textFieldEmailLogin.editText?.text.toString().trim()
            val password = binding.textFieldPassLogin.editText?.text.toString().trim()

            if (validate(email,password)) {
                startActivity(Intent(this, BlankActivity::class.java))
            }
        }

        binding.textViewHere.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    fun isValidEmail(email: String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()


    private fun validate(
        email: String,
        password: String
    ): Boolean {
        if (email.isEmpty()) {
            binding.textHelperEmailLogin.text = "Field tidak boleh kosong"
            binding.textHelperEmailLogin.visibility = View.VISIBLE
        } else if (!isValidEmail(email)) {
            binding.textHelperEmailLogin.text = "Masukkan email dengan benar"
        } else {
            binding.textHelperEmailLogin.visibility = View.GONE
        }

        if (password.isEmpty()) {
            binding.textHelperPassLogin.text = "Field tidak boleh kosong"
            binding.textHelperPassLogin.visibility = View.VISIBLE
        }  else if (password.length < 8 ) {
            binding.textHelperPassLogin.text = "Password minimal 8 karakter"
            binding.textHelperPassLogin.visibility = View.VISIBLE
        } else {
            binding.textHelperPassLogin.visibility = View.GONE
        }
        return true
    }
}