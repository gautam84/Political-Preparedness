package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.MainRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(application: Application) : AndroidViewModel(application) {

    private val mainRepository = MainRepository(application)

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    init {
        _address.value = Address("", "", "", "", "")
    }


    fun getRepresentativesByAddress(address: Address?) {
        viewModelScope.launch {
            _representatives.value = arrayListOf()
            if (address != null) {
                try {
                    _address.value = address
                    val (offices, officials) = mainRepository.getRepresentativesFromSource(
                        address,
                        true,
                        null,
                        null
                    )
                    _representatives.value =
                        offices.flatMap { office -> office.getRepresentatives(officials) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun loadRepresentatives() {
        viewModelScope.launch {
            getRepresentativesByAddress(_address.value)
        }
    }



}
