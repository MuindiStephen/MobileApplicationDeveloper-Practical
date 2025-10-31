package com.muindi.stephen.mobiledeveloperpractical.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.muindi.stephen.mobiledeveloperpractical.R
import com.muindi.stephen.mobiledeveloperpractical.databinding.FragmentGeneralAssessmentBinding

class GeneralAssessmentFragment : Fragment() {

    private lateinit var binding: FragmentGeneralAssessmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGeneralAssessmentBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }
}