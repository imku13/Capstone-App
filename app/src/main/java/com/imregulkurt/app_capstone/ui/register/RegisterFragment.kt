package com.imregulkurt.app_capstone.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.imregulkurt.app_capstone.R
import com.imregulkurt.app_capstone.common.gone
import com.imregulkurt.app_capstone.common.viewBinding
import com.imregulkurt.app_capstone.common.visible
import com.imregulkurt.app_capstone.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            registerButton.setOnClickListener {
                if (editTextTextPassword4.text.toString() == editTextTextPassword5.text.toString()){
                    viewModel.registerUser(
                        editTextTextEmailAddress4.text.toString(),
                        editTextTextPassword4.text.toString()
                    )
                } else {
                    Snackbar.make(requireView(), R.string.passwords_not_matched, 1000).show()
                }

            }
            alreadyRegisteredButton.setOnClickListener {
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.registerState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is RegisterState.Loading -> {
                    progressBar2.visible()
                }
                is RegisterState.GoToHome -> {
                    progressBar2.gone()
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToHomeFragment2())
                }
                is RegisterState.ShowPopUp -> {
                    progressBar2.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}
