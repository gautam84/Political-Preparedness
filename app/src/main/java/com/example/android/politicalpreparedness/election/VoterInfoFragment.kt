package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.google.android.material.snackbar.Snackbar

class VoterInfoFragment : Fragment() {

    private lateinit var viewModel: VoterInfoViewModel
    private lateinit var viewModelFactory: VoterInfoViewModelFactory
    private lateinit var binding: FragmentVoterInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentVoterInfoBinding.inflate(inflater)

        val electionDatabase = ElectionDatabase.getInstance(requireActivity())

        val args by navArgs<VoterInfoFragmentArgs>()


        viewModelFactory = VoterInfoViewModelFactory(electionDatabase.electionDao)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(VoterInfoViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        if (!(args.argDivision.country.isBlank() || args.argDivision.state.isBlank())) {
            viewModel.getVoterInfo(args.argElectionId, args.argDivision)
            viewModel.url.observe(viewLifecycleOwner, Observer {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(browserIntent)
                viewModel.navigationToUrlCompleted()
            })
        } else {
            showErrorSnackbar()
        }

        return binding.root

    }

    private fun showErrorSnackbar() {
        Snackbar.make(requireActivity().findViewById(R.id.nav_host_fragment), "Error Retrieving Information", Snackbar.LENGTH_LONG).show()
    }


}