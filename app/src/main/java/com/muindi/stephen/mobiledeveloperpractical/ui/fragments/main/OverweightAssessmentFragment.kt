package com.muindi.stephen.mobiledeveloperpractical.ui.fragments.main

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.muindi.stephen.mobiledeveloperpractical.R
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.visits.VisitsGeneralAssessmentRequest
import com.muindi.stephen.mobiledeveloperpractical.data.model.requests.visits.VisitsOverweightAssessmentRequest
import com.muindi.stephen.mobiledeveloperpractical.databinding.FragmentOverweightAssessmentBinding
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
import java.util.UUID

@AndroidEntryPoint
class OverweightAssessmentFragment : Fragment() {

    private lateinit var binding: FragmentOverweightAssessmentBinding
    private val viewModel: MainViewModel by viewModels()

    private var selectedGeneralHealth = "Good"
    private var selectedOnDrugs = "Yes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOverweightAssessmentBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRadioGroups()
        initBinding()
        subScribeAddGeneralAssessmentRemotelyStateObservables()

        val accessToken = getToken(requireContext())

        binding.buttonSave.setOnClickListener {
            if (validatedInputs()) {
                viewModel.addANewOverweightAssessmentInfoRemotely(
                    accessToken = "Bearer $accessToken",
                    general_health = selectedGeneralHealth,
                    on_diet = "null",
                    patient_id = binding.inputPatientName.text.toString().trim(),
                    visit_date = binding.inputVisitDate.text.toString().trim(),
                    vital_id = "10",
                    on_drugs = selectedOnDrugs,
                    comments = binding.inputComments.text.toString().trim()
                )

                try {
                    //trigger local storage
                    viewModel.addANewOverweightAssessmentInfoLocally(
                        visitsOverweightAssessmentRequest = VisitsOverweightAssessmentRequest(
                            general_health = selectedGeneralHealth,
                            on_diet = "null",
                            patient_id = binding.inputPatientName.text.toString().trim(),
                            visit_date = binding.inputVisitDate.text.toString().trim(),
                            vital_id = "10",
                            on_drugs = selectedOnDrugs,
                            comments = binding.inputComments.text.toString().trim()
                        )
                    )
                    displaySnackBar("Overweight Assessment visit created locally")
                } catch (e: Exception) {
                    Timber.tag("Add Overweight visit").e(
                        "Assessment=>failed." +
                                "ERROR==${e.message}"
                    )
                }
            }
        }
    }

    private fun initBinding() {
        binding.imageView11.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonClose.setOnClickListener {
            requireActivity().finishAffinity()
        }

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

    private fun subScribeAddGeneralAssessmentRemotelyStateObservables() {
        lifecycleScope.launch {
            viewModel.addNewOverweightAssessmentRemoteState.collect { state ->
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
                        val msg = apiResponse.data?.message ?: "Assessment added successful"
                        val status = apiResponse.data.slug

                        if (msg == "Visit Added Successfully") {
                            displaySnackBar(msg)

                            viewModel.resetOverWeightAssessState()

                            findNavController().navigate(
                                R.id.action_overweightAssessmentFragment_to_patientListingFragment
                            )
                        } else if (status != 0) {
                            displaySnackBar(msg ?: "Failed to add assessment")
                        }
                    }

                    null -> {

                    }
                }
            }
        }
    }

    private fun initRadioGroups() {
        setupRadioStyle(binding.radioGroup1, binding.radioGood, binding.radioBad) {
            selectedGeneralHealth = it
        }

        setupRadioStyle(binding.radioGroup2, binding.radio1, binding.radio2) {
            selectedOnDrugs = it
        }
    }

    private fun setupRadioStyle(
        group: RadioGroup,
        btn1: RadioButton,
        btn2: RadioButton,
        onSelect: (String) -> Unit
    ) {
        group.setOnCheckedChangeListener { g, checkedId ->
            val selected = g.findViewById<RadioButton>(checkedId)
            onSelect(selected.text.toString())

            for (i in 0 until g.childCount) {
                val rb = g.getChildAt(i) as RadioButton
                if (rb.id == checkedId) {
                    rb.setBackgroundResource(R.drawable.frame_51)
                    rb.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                } else {
                    rb.setBackgroundResource(R.drawable.frame_52)
                    rb.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                }
            }
        }
    }

    private fun validatedInputs(): Boolean {
        var valid = true

        if (binding.inputPatientName.text.isNullOrEmpty()) {
            binding.enterPatientName.error = "**required"
            valid = false
        }
        if (binding.inputVisitDate.text.isNullOrEmpty()) {
            binding.visitDate.error = "**required"
            valid = false
        }
        if (binding.inputComments.text.isNullOrEmpty()) {
            binding.enterComments.error = "**required"
            valid = false
        }

        return valid
    }
}