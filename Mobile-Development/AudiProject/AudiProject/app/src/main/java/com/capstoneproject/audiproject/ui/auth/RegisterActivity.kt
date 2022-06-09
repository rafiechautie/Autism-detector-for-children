package com.capstoneproject.audiproject.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import com.capstoneproject.audiproject.R
import com.capstoneproject.audiproject.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        setRegisterForm()
    }

    private fun setRegisterForm() {
        binding.apply {
            val fullname = formFullname.text
            val email = formEmail.text
            val password = formPassword.text
            val confirmPassword = formConfirmPass.text

            setButton(fullname, email, password, confirmPassword)
        }
    }

    private fun setButton(
        fullname: Editable?,
        email: Editable?,
        password: Editable?,
        confirmPassword: Editable?)
    {
        binding.apply {
            btnRegister.setOnClickListener {
                when {
                    fullname?.trim().toString().isEmpty() -> {
                        formFullname.error = "Kolom ini tidak boleh kosong!"
                        formFullname.requestFocus()
                        return@setOnClickListener
                    }
                    email?.trim().toString().isEmpty() -> {
                        formEmail.error = "Kolom ini tidak boleh kosong!"
                        formEmail.requestFocus()
                        return@setOnClickListener
                    }
                    password?.trim().toString().isEmpty() -> {
                        formPassword.error = "Kolom ini tidak boleh kosong!"
                        formPassword.requestFocus()
                        return@setOnClickListener
                    }
                    password?.trim().toString().length < 6 -> {
//                        formPassword.error = "Password harus minimal 6 karakter"
                        formPassword.requestFocus()
                        return@setOnClickListener
                    }
                    confirmPassword?.trim().toString().isEmpty() -> {
                        formConfirmPass.error = "Kolom ini tidak boleh kosong!"
                        formConfirmPass.requestFocus()
                        return@setOnClickListener
                    }
                    confirmPassword?.trim().toString() != password?.trim().toString() -> {
                        formConfirmPass.error = "Password tidak cocok!"
                        formConfirmPass.requestFocus()
                        return@setOnClickListener
                    }
                    else -> {
                        createUser(fullname.toString(), email.toString(), password.toString())
                    }
                }
            }

            btnSudahDaftar.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun createUser(fullname: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { create ->
            if (create.isSuccessful && create.result != null) {
                val user = create.result.user
                if (user != null) {
                    val request = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullname)
                        .build()
                    user.updateProfile(request).addOnCompleteListener {
                        startLoginActivity()
                    }
                } else {
                    Toast.makeText(this, "Register gagal!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, create.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}