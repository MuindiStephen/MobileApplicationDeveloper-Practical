package com.muindi.stephen.mobiledeveloperpractical.ui.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.muindi.stephen.mobiledeveloperpractical.R
import com.muindi.stephen.mobiledeveloperpractical.databinding.FragmentLoginBinding
import com.muindi.stephen.mobiledeveloperpractical.utils.ResourceNetwork
import com.muindi.stephen.mobiledeveloperpractical.utils.displaySnackBar
import com.muindi.stephen.mobiledeveloperpractical.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber

/**
 * Login user screen
 */
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        subScribeSignInStateObservables()

        binding.buttonSignIn.setOnClickListener {
            if (validatedInputs()) {
                viewModel.signInUser(
                    email = binding.inputLoginEmail.text.toString().trim(),
                    password = binding.inputLoginPassword.text.toString().trim()
                )
            }
        }
    }

    private fun subScribeSignInStateObservables() {
        lifecycleScope.launch {
            viewModel.signInState.collect { state ->
                when (state) {
                    is ResourceNetwork.Failure -> {
                        binding.progressBar.visibility = View.GONE

                        val errorJson = state.errorBody?.string()

                        val errorMsg = try {
                            JSONObject(errorJson ?: "")
                                .optString("message", "Login failed")
                        } catch (e: Exception) {
                            state.errorString ?: "Login failed"
                        }

                        displaySnackBar(errorMsg)
                    }

                    ResourceNetwork.Loading -> {
                        Timber.d("Loading request")
                        binding.progressBar.visibility = View.VISIBLE
                    }


                    is ResourceNetwork.Success -> {
                        binding.progressBar.visibility = View.GONE

                        val apiResponse = state.value
                        val message = apiResponse.message ?: "Login successful"

                        //then save the access token


                        displaySnackBar(message)

                        findNavController().navigate(
                            R.id.action_loginFragment_to_patientRegistrationFragment
                        )
                    }
                }
            }
        }
    }


    private fun validatedInputs(): Boolean {
        var valid = true

        if (binding.inputLoginEmail.text.isNullOrEmpty()) {
            binding.inputLoginEmail.error = "**required"
            valid = false
        }
        if (binding.inputLoginPassword.text.isNullOrEmpty()) {
            binding.inputLoginPassword.error = "**required"
            valid = false
        }

        return valid
    }
}