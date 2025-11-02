package com.muindi.stephen.mobiledeveloperpractical.ui.fragments.main

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.muindi.stephen.mobiledeveloperpractical.R
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.patients.PatientRegistrationRequest
import com.muindi.stephen.mobiledeveloperpractical.databinding.FragmentPatientRegistrationBinding
import com.muindi.stephen.mobiledeveloperpractical.utils.PreferencesHelper
import com.muindi.stephen.mobiledeveloperpractical.utils.ResourceNetwork
import com.muindi.stephen.mobiledeveloperpractical.utils.displaySnackBar
import com.muindi.stephen.mobiledeveloperpractical.utils.getToken
import com.muindi.stephen.mobiledeveloperpractical.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class PatientRegistrationFragment : Fragment() {
    private lateinit var binding: FragmentPatientRegistrationBinding
    private val viewModel: MainViewModel by viewModels()

    private var onBackPressedCallback: OnBackPressedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {  }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPatientRegistrationBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exitDialog()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, onBackPressedCallback!!
        )

        val preferencesHelper = PreferencesHelper(requireContext())

        val username = preferencesHelper.getUserName("name","")

        binding.textViewUserName.text = "Hi, "+ username.toString()


        initBinding()

        subScribeAddNewPatientRemotelyStateObservables()

        // defined accessToken here
        val accessToken = getToken(requireContext())

        binding.buttonSave.setOnClickListener {
            if (validatedInputs()) {
                viewModel.addANewPatientRemotely(
                    accessToken = "Bearer $accessToken",
                    unique = binding.inputPatientNumber.text.toString().trim(),
                    reg_date = binding.inputRegDate.text.toString().trim(),
                    firstname = binding.inputFirstName.text.toString().trim(),
                    lastname =  binding.inputLastName.text.toString().trim(),
                    dob =  binding.inputDOB.text.toString().trim(),
                    gender = binding.spinneGender.selectedItem.toString().trim()
                )


                try {
                    //trigger local storage
                    viewModel.addANewPatientLocally(
                        patientRegistrationRequest = PatientRegistrationRequest(
                            unique = binding.inputPatientNumber.text.toString().trim(),
                            reg_date = binding.inputRegDate.text.toString().trim(),
                            firstname = binding.inputFirstName.text.toString().trim(),
                            lastname =  binding.inputLastName.text.toString().trim(),
                            dob =  binding.inputDOB.text.toString().trim(),
                            gender = binding.spinneGender.selectedItem.toString().trim()
                        )
                    )
                    displaySnackBar("Patient created locally")
                } catch (e: Exception) {
                    Timber.tag("RegisterPatient").e(
                        "=>failed." +
                                "ERROR==${e.message}"
                    )
                }
            }
        }

    }

    private fun subScribeAddNewPatientRemotelyStateObservables() {
        lifecycleScope.launch {
            viewModel.addNewPatientRemoteState.collect { state ->
                when(state) {
                    is ResourceNetwork.Failure -> {
                        binding.progressBar.isVisible = false
                        Timber.e("Register Patient: Request failed")
                        val errorMsg = state.errorString ?: "Unexpected error occurred, please try again"
                        displaySnackBar(errorMsg)
                    }
                    ResourceNetwork.Loading -> {
                        Timber.d("Loading request....")
                        binding.progressBar.isVisible = true
                    }
                    is ResourceNetwork.Success -> {

                        binding.progressBar.isVisible = false

                        val apiResponse = state.value
                        val proceed = apiResponse.data?.proceed
                        val msg = apiResponse.data?.message ?: "Patient registration successful"

                        if (proceed == 0) {
                            displaySnackBar(msg)
                            findNavController().navigate(R.id.action_patientRegistrationFragment_to_vitalsFormFragment)
                        } else if (proceed == 1) {
                            displaySnackBar(msg ?: "Failed to register patient")
                        }
                    }

                    null -> {

                    }
                }
            }
        }
    }

    private fun initBinding() {

        binding.buttonClose.setOnClickListener {
            exitDialog()
        }

        binding.inputRegDate.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showDatePickerDialog()
            }
        }
        binding.inputDOB.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showDatePickerDialog2()
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->

                calendar.set(Calendar.YEAR, selectedYear)
                calendar.set(Calendar.MONTH, selectedMonth)
                calendar.set(Calendar.DAY_OF_MONTH, selectedDay)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.inputRegDate.setText(dateFormat.format(calendar.time))
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun showDatePickerDialog2() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->

                calendar.set(Calendar.YEAR, selectedYear)
                calendar.set(Calendar.MONTH, selectedMonth)
                calendar.set(Calendar.DAY_OF_MONTH, selectedDay)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.inputDOB.setText(dateFormat.format(calendar.time))
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun validatedInputs(): Boolean {
        var valid = true

        if (binding.inputPatientNumber.text.isNullOrEmpty()) {
            binding.enterPatientNumber.error = "**required"
            valid = false
        }
        if (binding.inputRegDate.text.isNullOrEmpty()) {
            binding.registrationDate.error = "**required"
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

        if (binding.inputDOB.text.isNullOrEmpty()) {
            binding.dateOfBirth.error = "**required"
            valid = false
        }

        return valid
    }

    fun exitDialog() {
        val builder = AlertDialog.Builder(requireContext()).setTitle("Confirm Exit!")
            .setMessage("Are you sure you want to exit the app?")
            .setPositiveButton("Yes") { _, _ ->
                requireActivity().finishAffinity()
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.setIcon(android.R.drawable.ic_dialog_alert).setCancelable(false)
        builder.show()
    }

}