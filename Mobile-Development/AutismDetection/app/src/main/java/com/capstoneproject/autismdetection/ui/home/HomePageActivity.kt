package com.capstoneproject.autismdetection.ui.home

import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.capstoneproject.autismdetection.R
import com.capstoneproject.autismdetection.databinding.ActivityHomePageBinding
import com.capstoneproject.autismdetection.ui.home.dashboard.HomeFragment
import com.capstoneproject.autismdetection.ui.home.detection.DetectionFragment
import com.capstoneproject.autismdetection.ui.home.profile.ProfileFragment


class HomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //inisialisasi seluruh fragment
        val fragmentDashboard = HomeFragment()
        val fragmentDetection = DetectionFragment()
        val fragmentProfile = ProfileFragment()

        //membuat fragment yang pertama kali muncul adalah fragment home
        setFragment(fragmentDashboard)

        binding.ivMenu1.setOnClickListener{
            setFragment(fragmentDashboard)

            changeIcon(binding.ivMenu1, R.drawable.ic_baseline_home_active)
            changeIcon(binding.ivMenu2, R.drawable.ic_sharp_qr_code_24)
            changeIcon(binding.ivMenu3, R.drawable.ic_outline_account_circle_24)
        }

        binding.ivMenu2.setOnClickListener{
            setFragment(fragmentDetection)

            changeIcon(binding.ivMenu1, R.drawable.ic_outline_home_24)
            changeIcon(binding.ivMenu2, R.drawable.ic_baseline_qr_code_scanner_active)
            changeIcon(binding.ivMenu3, R.drawable.ic_outline_account_circle_24)
        }

        binding.ivMenu3.setOnClickListener{
            setFragment(fragmentProfile)

            changeIcon(binding.ivMenu1, R.drawable.ic_outline_home_24)
            changeIcon(binding.ivMenu2, R.drawable.ic_sharp_qr_code_24)
            changeIcon(binding.ivMenu3, R.drawable.ic_baseline_account_circle_active)
        }
    }

    //fungsi untuk melakukan load dari sebuah fragment
    private fun setFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_frame, fragment)
        fragmentTransaction.commit()
    }

    //fungsi untuk mengubah icon ketika salah satu dari ketiga menu 1 - menu 3 di click
    private fun changeIcon(imageView: ImageView, int: Int){
        imageView.setImageResource(int)
    }
}