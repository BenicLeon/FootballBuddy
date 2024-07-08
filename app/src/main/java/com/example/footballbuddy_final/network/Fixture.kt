package com.example.footballbuddy_final.network



data class FixtureResponse(
    val response: List<Fixture>
)
data class Fixture(
    val fixture: FixtureDetails,
    val league: League,
    val teams: Teams,
    val goals: Goals
)

data class FixtureDetails(
    val id: Int,
    val referee: String?,
    val timezone: String,
    val date: String,
    val timestamp: Long,
    val venue: Venue,
    val status: Status
)
data class Venue(
    val id: Int?,
    val name: String,
    val city: String
)

data class Status(
    val long: String,
    val short: String,
    val elapsed: Int?
)

data class League(
    val id: Int,
    val name: String,
    val country: String,
    val logo: String,
    val flag: String,
    val season: Int,
    val round: String
)

data class Teams(
    val home: Team,
    val away: Team
)

data class Team(
    val id: Int,
    val name: String,
    val logo: String,
    val winner: Boolean?
)

data class Goals(
    val home: Int?,
    val away: Int?
)