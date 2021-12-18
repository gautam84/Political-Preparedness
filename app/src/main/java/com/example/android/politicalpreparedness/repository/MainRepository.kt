package com.example.android.politicalpreparedness.repository

import android.app.Application
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(application: Application) {

    private val electionDatabase = ElectionDatabase.getInstance(application)

    suspend fun getUpcomingElections(): List<Election> {
        return CivicsApi.retrofitService.getElections().elections
    }


    suspend fun getSavedElections(): List<Election>? {
        var savedElections: List<Election>?

        withContext(Dispatchers.IO) {
            savedElections = electionDatabase.electionDao.getAllElections().value
        }

        return savedElections
    }


    suspend fun getRepresentativesFromSource(
        address: Address,
        includeOffices: Boolean,
        levels: String?,
        roles: String?
    ): RepresentativeResponse {
        return CivicsApi.retrofitService.getRepresentatives(address.toFormattedString())
    }

}