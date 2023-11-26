package com.imregulkurt.app_capstone.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.imregulkurt.app_capstone.R
import com.imregulkurt.app_capstone.common.viewBinding
import com.imregulkurt.app_capstone.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val viewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
    }

    private fun observeData() {
        viewModel.splashState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SplashState.GoToSignIn -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
                is SplashState.GoToHome -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment2())
            }
        }
    }
}
