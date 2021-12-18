package com.example.android.politicalpreparedness.election

import android.view.View
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.*
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val dataSource: ElectionDao) : ViewModel() {

    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo


    private val _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url

    private val _isElectionSaved = MutableLiveData<Boolean>()
    val isElectionSaved: LiveData<Boolean>
        get() = _isElectionSaved


    fun getVoterInfo(electionId: Int, division: Division) {
        viewModelScope.launch {
            try {
                val savedElection: Election? = dataSource.getElectionById(electionId)
                _isElectionSaved.value = savedElection != null
                val voterInformationResponse =
                    CivicsApi.retrofitService.getVoterInfo(
                        getAddressFromDivision(division),
                        electionId
                    )
                _voterInfo.value = voterInformationResponse
                _election.value = voterInformationResponse.election

            } catch (e: Exception) {
                clear()
            }
        }
    }

    private fun getAddressFromDivision(division: Division): String {
        var output = division.country.plus("\n")

        if (division.state.isNotBlank()) {
            output = output.plus(division.state).plus("\n")
        }

        return output
    }

    fun toggleFollow() {
        viewModelScope.launch {
            _election.value?.let {
                if (_isElectionSaved.value == true) {

                    dataSource.deleteElectionById(it.id)
                    _isElectionSaved.value = false
                } else {

                    dataSource.insert(it)
                    _isElectionSaved.value = true
                }
            }
        }
    }


    fun navigateToFinderLocation() {
        _url.value =
            _voterInfo.value?.state?.first()?.electionAdministrationBody?.votingLocationFinderUrl
    }

    fun navigateToBallotLocation() {
        _url.value = _voterInfo.value?.state?.first()?.electionAdministrationBody?.ballotInfoUrl
    }

    fun visibility(): Int {
        return if (_voterInfo.value?.state?.first()?.electionAdministrationBody?.correspondenceAddress == null) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    fun getLabelText(isFollowed: Boolean) = if (!isFollowed) {
        "Follow"
    } else {
        "Unfollow"
    }


    fun navigationToUrlCompleted() {
        _url.value = null
    }


    private fun clear() {
        _election.value = null
        _voterInfo.value = null
    }
     val correpondenceAddress =_voterInfo.value?.state?.first()?.electionAdministrationBody?.correspondenceAddress?.toFormattedString()

}