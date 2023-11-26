package com.imregulkurt.app_capstone.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.imregulkurt.app_capstone.R
import com.imregulkurt.app_capstone.common.viewBinding
import com.google.firebase.auth.FirebaseAuth
import com.imregulkurt.app_capstone.common.gone
import com.imregulkurt.app_capstone.common.visible
import com.imregulkurt.app_capstone.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            backButton11.setOnClickListener {
                findNavController().navigateUp()
            }
            logoutButton.setOnClickListener {
                viewModel.logOut()
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSplashFragment())
            }
        }

        with(binding) {
            userEmailText.text = viewModel.getUserEmail()
        }
    }
}
