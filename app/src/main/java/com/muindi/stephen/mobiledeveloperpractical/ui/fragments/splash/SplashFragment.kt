package com.muindi.stephen.mobiledeveloperpractical.ui.fragments.splash

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.muindi.stephen.mobiledeveloperpractical.R
import com.muindi.stephen.mobiledeveloperpractical.databinding.FragmentSplashBinding
import com.muindi.stephen.mobiledeveloperpractical.utils.PreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private var isOnboardingDone by Delegates.notNull<Boolean>()
    private var ifFirstTimeLogin by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        navigate()
    }

    private fun navigate() {
        checkLoginHistory()
    }

    private fun checkLoginHistory() {
        isOnboardingDone = PreferencesHelper(requireActivity()).getIfToShowOnAuthDashboard(
            requireActivity()
        )
        if (isOnboardingDone) {
            Log.e("Onboarding","FirstTimeLogin onboardings, $isOnboardingDone")

            ifFirstTimeLogin = PreferencesHelper(requireContext()).getIfFirstTimeLogin(requireActivity())

            if (!ifFirstTimeLogin) {
                //go to register patient screen
                findNavController().navigate(
                    R.id.action_splashFragment_to_loginFragment
                )

            } else {
                //navigate to login screen
                findNavController().navigate(
                    R.id.action_splashFragment_to_patientRegistrationFragment
                )
            }
        } else {
            Log.e("Onboarding","FirstTimeLogin accounts $isOnboardingDone")
            findNavController().navigate(R.id.action_splashFragment_to_authDashboardFragment)
        }
    }
}