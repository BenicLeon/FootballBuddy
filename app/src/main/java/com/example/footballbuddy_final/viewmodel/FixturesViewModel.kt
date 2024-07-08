package com.example.footballbuddy_final.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footballbuddy_final.network.Fixture
import com.example.footballbuddy_final.network.FootballApi
import com.example.footballbuddy_final.network.TeamStatistics
import kotlinx.coroutines.launch


class FixturesViewModel : ViewModel() {
    private val _fixtures = MutableLiveData<List<Fixture>>()
    val fixtures: LiveData<List<Fixture>> get() = _fixtures


    private val _teamStatistics = MutableLiveData<TeamStatistics>()
    val teamStatistics: LiveData<TeamStatistics> get() = _teamStatistics


    fun fetchFixtures(baseURL: String, apiKey: String, teamId: Int, numberOfMatches: Int, isNext: Boolean) {
        val endpoint = if (isNext) "fixtures?team=$teamId&next=$numberOfMatches" else "fixtures?team=$teamId&last=$numberOfMatches"
        val url = "$baseURL$endpoint"

        viewModelScope.launch {
            try {
                val response = FootballApi.retrofitService.getFixtures(apiKey, url)
                if (response.isSuccessful) {
                    _fixtures.value = response.body()?.response ?: emptyList()
                    Log.d("FixturesViewModel", "Fetched fixtures successfully")
                } else {
                    Log.e("FixturesViewModel", "Failed to fetch fixtures: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("FixturesViewModel", "Exception occurred while fetching fixtures", e)
            }
        }
    }
    fun fetchTeamStatistics(baseURL: String, apiKey: String, teamId: Int, leagueId: Int, season: Int) {
        val url = "$baseURL/teams/statistics?league=$leagueId&season=$season&team=$teamId"

        viewModelScope.launch {
            try {
                val response = FootballApi.retrofitService.getTeamStatistics(apiKey, url)
                if (response.isSuccessful) {
                    _teamStatistics.value = response.body()?.response
                    Log.d("FixturesViewModel", "Fetched team statistics successfully")
                } else {
                    Log.e("FixturesViewModel", "Failed to fetch team statistics: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("FixturesViewModel", "Exception occurred while fetching team statistics", e)
            }
        }
    }
}