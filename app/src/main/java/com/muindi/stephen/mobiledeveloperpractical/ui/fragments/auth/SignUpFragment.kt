package com.muindi.stephen.mobiledeveloperpractical.ui.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.muindi.stephen.mobiledeveloperpractical.R
import com.muindi.stephen.mobiledeveloperpractical.databinding.FragmentSignUpBinding
import com.muindi.stephen.mobiledeveloperpractical.utils.ResourceNetwork
import com.muindi.stephen.mobiledeveloperpractical.utils.displaySnackBar
import com.muindi.stephen.mobiledeveloperpractical.utils.isValidEmail
import com.muindi.stephen.mobiledeveloperpractical.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber

/**
 * Sign up user screen
 */
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {  }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(
            inflater, container, false
        )

        initBinding()

        return binding.root
    }

    private fun initBinding() {
        binding.signInAlreadyAccText.setOnClickListener {
            findNavController().navigate(
                R.id.action_signUpFragment_to_loginFragment
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        subScribeSignUpStateObservables()

        binding.buttonSignUp.setOnClickListener {
            if (validatedInputs()) {
                viewModel.signUpUser(
                    email = binding.inputEmail.text.toString().trim(),
                    password = binding.inputPassword.text.toString().trim(),
                    firstname = binding.inputFirstName.text.toString().trim(),
                    lastname = binding.inputLastName.text.toString().trim()
                )
            }
        }
    }

    private fun subScribeSignUpStateObservables() {
        lifecycleScope.launch {
            viewModel.signUpState.collect { state ->
                when(state) {
                    is ResourceNetwork.Failure -> {
                        binding.progressBar.visibility = View.GONE
                        val errorMsg = try {
                            val errorJson = state.errorBody?.string()
                            val jsonObject = JSONObject(errorJson ?: "")
                            val errorsObj = jsonObject.optJSONObject("errors")


                            errorsObj?.keys()?.asSequence()?.firstOrNull()?.let { key ->
                                errorsObj.getJSONArray(key).getString(0)
                            } ?: jsonObject.optString("message", "Account Sign up failed")
                        } catch (e: Exception) {
                            state.errorString ?: "Account Sign up failed"
                        }

                        displaySnackBar(errorMsg)
                    }

                    ResourceNetwork.Loading -> {
                        Timber.d("Loading request")
                        binding.progressBar.visibility = View.VISIBLE

                    }


                    is ResourceNetwork.Success -> {
                       binding.progressBar.visibility = View.GONE

                        val apiData = state.value.data
                        val signupMsg = apiData?.message ?: "Account creation successful"

                        displaySnackBar(signupMsg)
                        findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                    }
                }
            }
        }
    }


    private fun validatedInputs(): Boolean {
        var valid = true

        if (binding.inputEmail.text.isNullOrEmpty()) {
            binding.enterEmail.error = "**required"
            valid = false
        }
        if (binding.inputPassword.text.isNullOrEmpty()) {
            binding.enterPassword.error = "**required"
            valid = false
        }
        if (binding.inputFirstName.text.isNullOrEmpty()) {
            binding.enterFirstName.error = "**required"
            valid = false
        }
        if (binding.inputLastName.text.isNullOrEmpty()) {
            binding.enterLastName.error = "**required"
            valid = false
        }

        if (!isValidEmail(binding.inputEmail.text.toString().trim())) {
            binding.enterEmail.error = "Email is invalid"
            valid = false
        }

        return valid
    }
}