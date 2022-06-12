package com.capstoneproject.audiproject.ui.home.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.audiproject.databinding.FragmentProfileBinding
import com.capstoneproject.audiproject.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var user: FirebaseUser


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataProfile()
        setButton()
    }

    private fun setDataProfile() {
        user = FirebaseAuth.getInstance().currentUser!!
        binding.fullname.text = user.displayName
        binding.email.text = user.email
    }

    private fun setButton() {
        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(activity, LoginActivity::class.java))
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}