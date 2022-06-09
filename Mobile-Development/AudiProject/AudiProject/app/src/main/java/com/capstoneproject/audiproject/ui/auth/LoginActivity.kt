package com.capstoneproject.audiproject.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import com.capstoneproject.audiproject.MainActivity
import com.capstoneproject.audiproject.R
import com.capstoneproject.audiproject.databinding.ActivityLoginBinding
import com.capstoneproject.audiproject.ui.detection.AutismDetectionActivity

import com.capstoneproject.audiproject.ui.home.HomePageActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSign: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        setLoginForm()
        setLoginWithGoogle()
    }

    private fun setLoginWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSign = GoogleSignIn.getClient(this, gso)
    }

    private fun setLoginForm() {
        binding.apply {
            val email = formEmail.text
            val password = formPassword.text

            setButton(email, password)
        }
    }

    private fun setButton(
        email: Editable?,
        password: Editable?)
    {
        binding.apply {
            btnLogin.setOnClickListener {
                when {
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
                    else -> {
                        loginUser(email.toString(), password.toString())
                    }
                }
            }

            btnLoginWithGoogle.setOnClickListener {
                loginGoogle()
            }

            btnBelumDaftar.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                finish()
            }

            btnForgotPassword.setOnClickListener {
                startActivity(Intent(this@LoginActivity, ResetPasswordActivity::class.java))
            }
        }
    }

    private fun loginGoogle() {
        val signInIntent = googleSign.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { login ->
            if (login.isSuccessful && login.result != null) {
                Toast.makeText(this, "Login sukses!", Toast.LENGTH_SHORT).show()
                if (login.result.user != null) {
                    startHomeActivity()
                } else {
                    Toast.makeText(this, "Login terlebih dahulu!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, login.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startHomeActivity() {
        startActivity(Intent(this, HomePageActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)
                    firabaseAuthWithGoogle(account.idToken)
                } catch (e: ApiException) {
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firabaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startHomeActivity()
            } else {
                Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

 /*   override fun onStart() {
        super.onStart()
        val checkSession = auth.currentUser
        if (checkSession != null) {
            startHomeActivity()
        }
    }*/

    companion object {
        private const val RC_SIGN_IN = 120
    }
}