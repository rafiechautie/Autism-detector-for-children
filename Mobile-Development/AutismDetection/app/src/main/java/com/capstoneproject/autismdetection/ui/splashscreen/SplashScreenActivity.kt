package com.capstoneproject.autismdetection.ui.splashscreen

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import com.capstoneproject.autismdetection.R
import com.capstoneproject.autismdetection.databinding.ActivitySplashScreenBinding
import com.capstoneproject.autismdetection.ui.home.HomePageActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //membuat view pada splashscreen jadi full (memmenuhi layar)
        setUpView()

        //membuat delay
        var handler = Handler()
        handler.postDelayed({
            //mengarahkan ke halaman homepage
            var intent = Intent(this@SplashScreenActivity, HomePageActivity::class.java)
            startActivity(intent)
            finish()
        }, DELAY)
    }

    private fun setUpView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    companion object{
        private const val DELAY = 3000L
    }


}