package com.muindi.stephen.mobiledeveloperpractical.ui.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.muindi.stephen.mobiledeveloperpractical.R
import com.muindi.stephen.mobiledeveloperpractical.databinding.FragmentAuthDashboardBinding
import com.muindi.stephen.mobiledeveloperpractical.utils.PreferencesHelper


class AuthDashboardFragment : Fragment() {
    private lateinit var binding: FragmentAuthDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {  }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthDashboardBinding.inflate(
            inflater, container, false
        )

        binding.apply {
            buttonSignIn.setOnClickListener {
                PreferencesHelper(requireActivity()).setIfToShowOnAuthDashboard(requireActivity(), false)

                findNavController().navigate(
                    R.id.action_authDashboardFragment_to_loginFragment
                )
            }
            buttonSignUp.setOnClickListener {
                PreferencesHelper(requireActivity()).setIfToShowOnAuthDashboard(requireActivity(), false)

                findNavController().navigate(
                    R.id.action_authDashboardFragment_to_signUpFragment
                )
            }
        }

        return binding.root
    }
}