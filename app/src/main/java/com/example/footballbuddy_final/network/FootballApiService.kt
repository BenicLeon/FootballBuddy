package com.example.footballbuddy_final.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url






interface FootballApiService {
    @GET
    suspend fun getFixtures(
        @Header("x-apisports-key") apiKey: String,
        @Url url: String
    ): Response<FixtureResponse>

    @GET
    suspend fun getTeamStatistics(
        @Header("x-apisports-key") apiKey: String,
        @Url url: String
    ): Response<TeamStatisticsResponse>
}








object FootballApi {
    private const val BASE_URL = "https://v3.football.api-sports.io/"


    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val retrofitService: FootballApiService by lazy {
        retrofit.create(FootballApiService::class.java)
    }
}