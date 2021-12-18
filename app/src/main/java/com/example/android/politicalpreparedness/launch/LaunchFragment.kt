package com.example.android.politicalpreparedness.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding


class LaunchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentLaunchBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.representativeButton.setOnClickListener { navToRepresentatives() }
        binding.upcomingButton.setOnClickListener { navToElections() }

        return binding.root
    }

    private fun navToElections() {
        this.findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToElectionsFragment())
    }

    private fun navToRepresentatives() {
        this.findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToRepresentativeFragment())
    }

}
