package com.imregulkurt.app_capstone.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.imregulkurt.app_capstone.R
import com.imregulkurt.app_capstone.common.gone
import com.imregulkurt.app_capstone.common.viewBinding
import com.imregulkurt.app_capstone.common.visible
import com.imregulkurt.app_capstone.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            loginButton.setOnClickListener{
                viewModel.signIn(
                    email=editTextTextEmailAddress.text.toString(),
                    password = editTextTextPassword.text.toString()
                )
            }
            notAUserButton.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoginState.Loading -> {
                    progressBar3.visible()
                }
                is LoginState.GoToHome -> {
                    progressBar3.gone()
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment2())
                }
                is LoginState.ShowPopUp -> {
                    progressBar3.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}
