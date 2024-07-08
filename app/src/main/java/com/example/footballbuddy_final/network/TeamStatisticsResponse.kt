package com.example.footballbuddy_final.network

data class TeamStatisticsResponse(
    val response: TeamStatistics
)

data class TeamStatistics(
    val league: LeagueName,
    val team: TeamName,
    val form: String,
    val fixtures: Fixtures,
    val goals: GoalsNumber
)

data class LeagueName(
    val id: Int,
    val name: String,
    val logo: String,
    val season: Int
)

data class TeamName(
    val id: Int,
    val name: String,
    val logo: String
)

data class Fixtures(
    val played: FixtureDetails2,
    val wins: FixtureDetails2,
    val draws: FixtureDetails2,
    val loses: FixtureDetails2
)

data class FixtureDetails2(
    val home: Int,
    val away: Int,
    val total: Int
)

data class GoalsNumber(
    val `for`: GoalDetails,
    val against: GoalDetails
)

data class GoalDetails(
    val total: GoalTotal,
    val average: GoalAverage
)

data class GoalTotal(
    val home: Int,
    val away: Int,
    val total: Int
)

data class GoalAverage(
    val home: String,
    val away: String,
    val total: String
)