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
import com.capstoneproject.audiproject.ui.home.map.MapViewModel
import com.capstoneproject.audiproject.ui.home.map.MapsTherapyActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.io.IOException


class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: FirebaseUser
    private lateinit var uri: Uri
    private lateinit var arrayListMaps: ArrayList<IndonesiaItem>
    private lateinit var viewModel: MapViewModel


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

        user = FirebaseAuth.getInstance().currentUser!!
        binding.username.text = user.displayName

        setupButton()

        viewModel = ViewModelProvider(this)[MapViewModel::class.java]

        viewModel.findLocation()

        viewModel.findLocation()

        viewModel.listLocation.observe(viewLifecycleOwner){
            binding.apply {
                arrayListMaps = ArrayList()
                arrayListMaps.addAll(it)
            }
        }

        binding.ivMapsTherapy.setOnClickListener(this)
    }

    private fun setupButton() {
        binding.apply {

            btnDetection.setOnClickListener {
                ImagePicker.Companion.with(this@HomeFragment)
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start(2)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            uri = data.data!!
            try {
                val intent = Intent(requireActivity(), FiturDetection::class.java)
                intent.putExtra(FiturDetection.URI_IMAGE, uri.toString())
                startActivity(intent)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    override fun onClick(view: View) {
        when{
            view.id == R.id.iv_maps_therapy -> {
                val intent = Intent(requireActivity(), MapsTherapyActivity::class.java)
                intent.putExtra(ARRAY_LIST_MAPS, arrayListMaps)
                startActivity(intent)
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