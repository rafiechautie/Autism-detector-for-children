package com.capstoneproject.audiproject.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.audiproject.R
import com.capstoneproject.audiproject.data.remote.response.IndonesiaItem
import com.capstoneproject.audiproject.databinding.ActivityHomePageBinding
import com.capstoneproject.audiproject.ui.home.dashboard.HomeFragment
import com.capstoneproject.audiproject.ui.home.profile.ProfileFragment

class HomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        //inisialisasi seluruh fragment
        val fragmentDashboard = HomeFragment()
        val fragmentProfile = ProfileFragment()

        //membuat fragment yang pertama kali muncul adalah fragment home
        setFragment(fragmentDashboard)

        binding.ivMenu1.setOnClickListener{
            setFragment(fragmentDashboard)

            changeIcon(binding.ivMenu1, R.drawable.ic_baseline_home_active)
            changeIcon(binding.ivMenu3, R.drawable.ic_outline_account_circle_24)
        }


        binding.ivMenu3.setOnClickListener{
            setFragment(fragmentProfile)

            changeIcon(binding.ivMenu1, R.drawable.ic_outline_home_24)
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