package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg election: Election)

    @Query("SELECT * FROM election_table")
    fun getAllElections(): LiveData<List<Election>>

    @Query("SELECT * from election_table WHERE id = :id")
    suspend fun getElectionById(id: Int): Election?

    @Query("DELETE FROM election_table WHERE id = :id")
    suspend fun deleteElectionById(id: Int)

    @Query("DELETE FROM election_table")
    suspend fun clear()

}