package com.muindi.stephen.mobiledeveloperpractical.ui.fragments.main

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.muindi.stephen.mobiledeveloperpractical.R
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.vitals.AddVitalRequest
import com.muindi.stephen.mobiledeveloperpractical.databinding.FragmentVitalsFormBinding
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
class VitalsFormFragment : Fragment() {

    private lateinit var binding: FragmentVitalsFormBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {  }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVitalsFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        autoCalculateBMI()
        subScribeAddVitalsRemotelyStateObservables()

        // defined accessToken here
        val accessToken = getToken(requireContext())

        binding.buttonSave.setOnClickListener {
            if (validatedInputs()) {
                viewModel.addNewVitalRemotely(
                    accessToken = "Bearer $accessToken",
                        bmi = binding.inputBMI.text.toString(),
                        height = binding.inputHeight.text.toString(),
                        patient_id = binding.inputPatientName.text.toString(),
                        visit_date = binding.inputVisitDate.text.toString(),
                        weight = binding.inputWeight.text.toString()
                )

                try {
                    //trigger local storage
                    viewModel.addANewVitalLocally(
                        vitalRequest = AddVitalRequest(
                            bmi = binding.inputBMI.text.toString(),
                            height = binding.inputHeight.text.toString(),
                            patient_id = binding.inputPatientName.text.toString(),
                            visit_date = binding.inputVisitDate.text.toString(),
                            weight = binding.inputWeight.text.toString()
                        )
                    )
                    displaySnackBar("Vital created locally")
                } catch (e: Exception) {
                    Timber.tag("Add vital").e(
                        "=>failed." +
                                "ERROR==${e.message}"
                    )
                }
            }
        }
    }

    private fun subScribeAddVitalsRemotelyStateObservables() {
        lifecycleScope.launch {
            viewModel.addNewVitalRemoteState.collect { state ->
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
                        val msg = apiResponse.data?.message ?: "Vital added successful"
                        val status = apiResponse.data.slug

                        if (msg == "Vital Added Successfully") {
                            displaySnackBar(msg)

                            //navigate either to general or overweight screens
                            //BMI comparison with 25
                            val bmi = binding.inputBMI.text.toString().toDouble()

                            if (bmi <= 25.0) {
                                findNavController().navigate(
                                   R.id.action_vitalsFormFragment_to_generalAssessmentFragment
                                )
                            } else {
                                findNavController().navigate(
                                    R.id.action_vitalsFormFragment_to_overweightAssessmentFragment
                                )
                            }

                        } else if (status != 0) {
                            displaySnackBar(msg ?: "Failed to add vital")
                        }
                    }

                    null -> {

                    }
                }
            }
        }
    }

    private fun initBinding() {
        binding.inputVisitDate.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showDatePickerDialog()
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
                binding.inputVisitDate.setText(dateFormat.format(calendar.time))
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun autoCalculateBMI() {
        binding.inputBMI.isEnabled = false
        binding.inputBMI.isFocusable = false

        binding.inputHeight.addTextChangedListener { calculateBMI() }
        binding.inputWeight.addTextChangedListener { calculateBMI() }
    }

    private fun calculateBMI() {
        val heightCm = binding.inputHeight.text.toString().toDoubleOrNull()
        val weightKg = binding.inputWeight.text.toString().toDoubleOrNull()

        if (heightCm != null && weightKg != null && heightCm > 0) {
            val heightM = heightCm / 100
            val bmi = weightKg / (heightM * heightM)
            binding.inputBMI.setText(String.format(Locale.US, "%.2f", bmi))
        }
    }

    private fun validatedInputs(): Boolean {
        var valid = true

        if (binding.inputPatientName.text.isNullOrEmpty()) {
            binding.enterBMI.error = "**required"
            valid = false
        }
        if (binding.inputVisitDate.text.isNullOrEmpty()) {
            binding.visitDate.error = "**required"
            valid = false
        }
        if (binding.inputHeight.text.isNullOrEmpty()) {
            binding.enterHeight.error = "**required"
            valid = false
        }
        if (binding.inputWeight.text.isNullOrEmpty()) {
            binding.enterWeight.error = "**required"
            valid = false
        }

        if (binding.inputBMI.text.isNullOrEmpty()) {
            binding.enterBMI.error = "**required"
            valid = false
        }

        return valid
    }
}