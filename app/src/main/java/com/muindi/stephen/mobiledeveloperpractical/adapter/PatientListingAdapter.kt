package com.muindi.stephen.mobiledeveloperpractical.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muindi.stephen.mobiledeveloperpractical.data.dto.responses.patients.RegisterPatientData
import com.muindi.stephen.mobiledeveloperpractical.databinding.PatientListItemRowBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * PatientListingAdapter
 */
class PatientListingAdapter : RecyclerView.Adapter<PatientListingAdapter.TaskViewHolder>() {

    private var patientListItems: List<RegisterPatientData> = ArrayList()

    inner class TaskViewHolder(private val binding: PatientListItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(patientListItems: RegisterPatientData) {
            binding.textView75.text = patientListItems.firstname + " " + patientListItems.lastname

            val patientDOB = patientListItems.dob
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dob = sdf.parse(patientDOB)

            val dobCalendar = Calendar.getInstance()
            if (dob != null) {
                dobCalendar.time = dob
            }

            val today = Calendar.getInstance()

            var age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR)

            if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--
            }

            binding.textView76.text = age.toString()
            binding.textView77.text = "Normal"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =
            PatientListItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = patientListItems[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return patientListItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(patientListItemsNew: List<RegisterPatientData>) {
        patientListItems = patientListItemsNew
        notifyDataSetChanged()
    }
}