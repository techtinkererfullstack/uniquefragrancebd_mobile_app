package com.example.uniquefragrancebd.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.uniquefragrancebd.R
import com.example.uniquefragrancebd.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Custom back button handling
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navigate to ProductListFragment instead of going back to Login
                findNavController().navigate(R.id.action_signupFragment_to_productListFragment)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignup.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val email = binding.etSignupEmail.text.toString()
            val phone = binding.etPhone.text.toString()
            val address = binding.etAddress.text.toString()
            val password = binding.etSignupPassword.text.toString()
            
            viewModel.signup(
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password,
                phone = phone,
                address = address
            )
        }

        binding.tvGoToLogin.setOnClickListener {
            findNavController().popBackStack()
        }

        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.signupProgressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                    
                    if (state.isSuccess) {
                        Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
                        // Use the action that clears the stack and goes to Products
                        findNavController().navigate(R.id.action_signupFragment_to_productListFragment)
                    }

                    state.error?.let { error ->
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                        viewModel.resetState()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}