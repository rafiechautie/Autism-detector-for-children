package com.capstoneproject.audiproject.ui.home.consultation

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.capstoneproject.audiproject.R
import com.capstoneproject.audiproject.databinding.ActivityConsulatationBinding


class ConsultationActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityConsulatationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsulatationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.btnTherapyProfessional.setOnClickListener(this)

        binding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    override fun onClick(view: View?) {
        when {
            view?.id == R.id.btn_therapy_professional -> {
                val number = "+62 85162656322"
                val url = "https://api.whatsapp.com/send?phone=$number"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        }
    }
}