package com.capstoneproject.audiproject.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.capstoneproject.audiproject.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkEmail()
    }

    private fun checkEmail() {
        binding.apply {
            val email = formEmail.text.toString().trim()

            if (email.isEmpty()) {
                formEmail.error = "Kolom ini tidak boleh kosong"
                formEmail.requestFocus()
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                formEmail.error = "Email tidak valid"
                formEmail.requestFocus()
            }

            resetPassword(email)
        }
    }

    private fun resetPassword(email: String) {
        binding.apply {
            btnSendLink.setOnClickListener {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { process ->
                    if (process.isSuccessful) {
                        Toast.makeText(this@ResetPasswordActivity, "Silahkan cek email untuk link reset password", Toast.LENGTH_SHORT).show()
                        Intent(this@ResetPasswordActivity, LoginActivity::class.java).also {
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                        }
                    } else {
                        Toast.makeText(this@ResetPasswordActivity, "${process.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }
}