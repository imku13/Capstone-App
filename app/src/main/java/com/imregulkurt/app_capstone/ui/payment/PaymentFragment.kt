package com.imregulkurt.app_capstone.ui.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.imregulkurt.app_capstone.R
import com.imregulkurt.app_capstone.common.viewBinding
import com.imregulkurt.app_capstone.databinding.FragmentPaymentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private val binding by viewBinding(FragmentPaymentBinding::bind)
    private val viewModel by viewModels<PaymentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            backButton9.setOnClickListener {
                findNavController().navigateUp()
            }

            // purchase button listener here...
        }

        observeData()
    }

    private fun observeData() {
        viewModel.paymentState.observe(viewLifecycleOwner) { state ->
            when(state) {
                is PaymentState.Loading -> {
                    // progress bar should be set to hide here...
                }
                is PaymentState.SuccessState -> {

                }
                is PaymentState.ShowPopUp -> {
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}
