package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import java.util.*

class ElectionsFragment : Fragment() {

    private val viewModel: ElectionsViewModel by lazy {
        ViewModelProvider(this, ElectionsViewModelFactory(requireActivity().application)).get(
            ElectionsViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentElectionBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToVoterInfoFragment.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                        it.id,
                        it.division
                    )
                )
                viewModel.navigationComplete()
            }
        })

        val savedElectionsAdapter = ElectionListAdapter(ElectionListener {
            viewModel.navigateToVoterInfoFragment.value = it

        })

        val upcomingElectionsAdapter = ElectionListAdapter(ElectionListener {
            viewModel.navigateToVoterInfoFragment.value = it

        })

        binding.upcomingElectionsList.adapter = upcomingElectionsAdapter
        binding.savedElectionsList.adapter = savedElectionsAdapter


        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer {
            upcomingElectionsAdapter.submitList(it)
        })
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadUpcomingElections()
        viewModel.loadSavedElections()
    }

}