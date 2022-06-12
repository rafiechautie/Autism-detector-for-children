package com.capstoneproject.audiproject.ui.home.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.audiproject.R
import com.capstoneproject.audiproject.data.remote.response.IndonesiaItem
import com.capstoneproject.audiproject.databinding.FragmentHomeBinding
import com.capstoneproject.audiproject.ui.detection.FiturDetection
import com.capstoneproject.audiproject.ui.home.consultation.ConsultationActivity
import com.capstoneproject.audiproject.ui.home.map.MapViewModel
import com.capstoneproject.audiproject.ui.home.map.MapsTherapyActivity
import com.capstoneproject.audiproject.utils.button.ButtonDetection
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.io.IOException


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: FirebaseUser
    private lateinit var uri: Uri
    private lateinit var arrayListMaps: ArrayList<IndonesiaItem>
    private lateinit var viewModel: MapViewModel

    private lateinit var btnDetection: View
    private lateinit var btnNearestTheraphy: View
    private lateinit var btnConsultation: View


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUsername()

        btnDetection = view.findViewById(R.id.btnDetection)
        btnNearestTheraphy = view.findViewById(R.id.btnNearestTheraphy)
        btnConsultation = view.findViewById(R.id.btnConsultation)

        setupButton()

        viewModel = ViewModelProvider(this)[MapViewModel::class.java]
        viewModel.findLocation()

        viewModel.listLocation.observe(viewLifecycleOwner){
            binding.apply {
                arrayListMaps = ArrayList()
                arrayListMaps.addAll(it)
            }
        }
    }

    private fun setUsername() {
        user = FirebaseAuth.getInstance().currentUser!!
        val stringUsername = "Hello," + System.getProperty("line.separator") + user.displayName
        binding.username.text = stringUsername
    }

    private fun setupButton() {
        btnDetection.setOnClickListener {
            ImagePicker.Companion.with(this@HomeFragment)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(2)
        }

        btnNearestTheraphy.setOnClickListener {
            val intent = Intent(requireActivity(), MapsTherapyActivity::class.java)
            intent.putExtra(ARRAY_LIST_MAPS, arrayListMaps)
            startActivity(intent)
        }

        btnConsultation.setOnClickListener {
            startActivity(Intent(requireActivity(), ConsultationActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            uri = data.data!!
            try {
                val intent = Intent(activity, FiturDetection::class.java)
                intent.putExtra(FiturDetection.URI_IMAGE, uri.toString())
                startActivity(intent)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARRAY_LIST_MAPS = "array_list_maps"
    }

}