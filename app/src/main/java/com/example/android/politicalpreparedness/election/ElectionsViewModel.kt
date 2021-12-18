package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.MainRepository
import kotlinx.coroutines.launch

class ElectionsViewModel(application: Application) : ViewModel() {

    private val mainRepository = MainRepository(application)


    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    private val _navigateToVoterInfoFragment = MutableLiveData<Election>()
    val navigateToVoterInfoFragment: MutableLiveData<Election>
        get() = _navigateToVoterInfoFragment


    fun loadUpcomingElections() {
        viewModelScope.launch {
            try {
                val elections = mainRepository.getUpcomingElections()
                _upcomingElections.value = elections

                Log.d(".", elections.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadSavedElections() {
        viewModelScope.launch {
            try {
                _savedElections.value = mainRepository.getSavedElections()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun navigationComplete() {
        _navigateToVoterInfoFragment.value = null
    }


    init {
        loadUpcomingElections()
        loadSavedElections()
    }


}