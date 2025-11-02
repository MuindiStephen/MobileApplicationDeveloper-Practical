package com.muindi.stephen.mobiledeveloperpractical.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.muindi.stephen.mobiledeveloperpractical.databinding.FragmentPatientListingBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PatientListingFragment : Fragment() {

    private lateinit var binding: FragmentPatientListingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
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
}