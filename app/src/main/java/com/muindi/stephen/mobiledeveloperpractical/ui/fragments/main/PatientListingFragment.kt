package com.muindi.stephen.mobiledeveloperpractical.ui.fragments.main

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muindi.stephen.mobiledeveloperpractical.adapter.PatientListingAdapter
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.patients.RegisterPatientData
import com.muindi.stephen.mobiledeveloperpractical.databinding.FragmentPatientListingBinding
import com.muindi.stephen.mobiledeveloperpractical.databinding.FragmentVitalsFormBinding
import com.muindi.stephen.mobiledeveloperpractical.utils.displaySnackBar
import com.muindi.stephen.mobiledeveloperpractical.utils.getToken
import com.muindi.stephen.mobiledeveloperpractical.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class PatientListingFragment : Fragment() {

    private lateinit var binding: FragmentPatientListingBinding
    private val viewModel: MainViewModel by viewModels()

    private val patientListAdapter by lazy { PatientListingAdapter() }
    private var allPatientList = listOf<RegisterPatientData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {  }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPatientListingBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()

        getAllPatientListing()
    }

    private fun getAllPatientListing() {
        val accessToken = getToken(requireContext())

        viewModel.fetchAllPatientListing(accessToken = accessToken!!).observe(viewLifecycleOwner) { response ->
            if (response?.data.isNullOrEmpty()) {
                displaySnackBar("No patients found")
            } else {
                allPatientList = response.data

                patientListAdapter.submitList(allPatientList)
                binding.recyclerViewProductListing.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = patientListAdapter
                }
            }
        }

    }

    private fun initBinding() {
        binding.inputPickDate.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showDatePickerDialog()
            }
        }

        binding.inputPickDate.setOnEditorActionListener { _, _, _ ->
            filterRecordsByVisitDate()
            false
        }
    }

    private fun filterRecordsByVisitDate() {
        val selectedDate = binding.inputPickDate.text.toString().trim()
        if (selectedDate.isEmpty()) {
            patientListAdapter.submitList(allPatientList)
            return
        }

        val filteredList = allPatientList.filter { it.reg_date == selectedDate }

        if (filteredList.isEmpty()) {
            displaySnackBar("No patient records for $selectedDate")
        }

        patientListAdapter.submitList(filteredList)
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
                binding.inputPickDate.setText(dateFormat.format(calendar.time))
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

}