package com.example.footballbuddy_final

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.footballbuddy_final.network.Fixtures
import com.example.footballbuddy_final.network.TeamStatistics
import com.example.footballbuddy_final.viewmodel.FixturesViewModel


@Composable
fun StatisticsScreen(navController: NavController, teamId: Int, fixturesViewModel: FixturesViewModel = viewModel()) {
    var leagueId by remember { mutableIntStateOf(2) }
    var season by remember { mutableIntStateOf(2021) }
    val apiBASEURL = "https://v3.football.api-sports.io/"
    val userAPIKEY = "1ead3604a17f7d9186490de5a854af3e"
    val teamStatistics by fixturesViewModel.teamStatistics.observeAsState()

    var showDialog by remember { mutableStateOf(false) }
    val leagues = listOf(
        "World Cup" to 1,
        "UEFA Champions League" to 2,
        "UEFA Europa League" to 3,
        "UEFA Nations League" to 5,
        "FIFA Club World Cup" to 15,
        "Premier League" to 39,
        "La Liga" to 140,
        "Ligue 1" to 61,
        "Serie A" to 135,
        "Bundesliga" to 78,
        "Super League 1" to 197,
        "Süper Lig" to 203,
        "Jupiler Pro League" to 144,
        "Primeira Liga" to 94,
        "HNL" to 210,
    )
    var selectedLeagueName by remember { mutableStateOf(leagues[0].first) }
    var newLeagueId by remember { mutableIntStateOf(leagues[0].second) }

    LaunchedEffect(Unit) {
        fixturesViewModel.fetchTeamStatistics(apiBASEURL, userAPIKEY, teamId, leagueId, season)
    }
    BackButton(navController = navController)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = season.toString(),
                onValueChange = { season = it.toIntOrNull() ?: season },
                label = { Text("Season") },
                textStyle = TextStyle(color = Color.Black),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    fixturesViewModel.fetchTeamStatistics(apiBASEURL, userAPIKEY, teamId, leagueId, season)
                },
                modifier = Modifier.padding(start = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )
            ) {
                Text("Submit")
            }
        }

        Button(
            onClick = {
                showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            Text("Change Competition")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (teamStatistics == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading statistics...")
            }
        } else if (teamStatistics!!.league.name == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Competition wasn't in this season", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Red)
            }
        } else {
            TeamStatisticsItem(statistics = teamStatistics!!)
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Available Leagues",
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(fontSize = 18.sp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LazyColumn(modifier = Modifier.height(400.dp)) {
                        items(leagues) { (leagueName, leagueId) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        selectedLeagueName = leagueName
                                        newLeagueId = leagueId
                                    }
                            ) {
                                Text(text = leagueName)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            leagueId = newLeagueId // Update the leagueId here too
                            fixturesViewModel.fetchTeamStatistics(apiBASEURL, userAPIKEY, teamId, leagueId, season)
                            showDialog = false
                        },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Composable
fun TeamStatisticsItem(statistics: TeamStatistics) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(2.dp)
            .shadow(8.dp, shape = RoundedCornerShape(6.dp))  // Adding shadow
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(6.dp))
                ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(vertical = 4.dp)
        ) {
            androidx.compose.foundation.Image(
                painter = rememberAsyncImagePainter(statistics.league.logo),
                contentDescription = null,
                modifier = Modifier.size(40.dp) // Make the image bigger
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${statistics.league.name} (${statistics.league.season})",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp // Make the text bigger
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Check if all fixture items are null or zero
        val fixtures = statistics.fixtures
        val allFixturesNull = fixtures.played.total == 0 &&
                fixtures.wins.total == 0 &&
                fixtures.draws.total == 0 &&
                fixtures.loses.total == 0

        if (allFixturesNull) {

            Text(
                text = "Club didn't play in this competition/season",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp, // Make the text bigger
                color = Color.Red // Optional: Change text color to indicate the message
            )
        } else {
            // Display fixture items
            StatisticsSection(fixtures = fixtures, statistics = statistics)
        }
    }
}

@Composable
fun StatisticsSection(fixtures: Fixtures, statistics: TeamStatistics) {
    val goalsHome = statistics.goals.`for`.total.home
    val goalsAway = statistics.goals.`for`.total.away

    val totalGoals = goalsHome+goalsAway

    val goalsHomeConceded = statistics.goals.against.total.home
    val goalsAwayConceded = statistics.goals.against.total.away

    val totalGoalsConceded = goalsHomeConceded+goalsAwayConceded
    Column {
        StatisticsRow(label = "Matches", value = fixtures.played.total.toString())
        StatisticsRow(label = "Goals scored", value = totalGoals.toString())
        StatisticsRow(label = "Goals conceded", value = totalGoalsConceded.toString())
        StatisticsRow(label = "Wins", value = statistics.fixtures.wins.total.toString())
        StatisticsRow(label = "Draws", value = statistics.fixtures.draws.total.toString())
        StatisticsRow(label = "Losses", value = statistics.fixtures.loses.total.toString())


        Spacer(modifier = Modifier.height(8.dp))

        FormSection(form = statistics.form)
    }
}

@Composable
fun StatisticsRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Normal, fontSize = 16.sp)
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun FormSection(form: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        form.split("").forEach { result ->
            when (result) {
                "W" -> FormBox(result, Color.Green)
                "D" -> FormBox(result, Color.Gray)
                "L" -> FormBox(result, Color.Red)
                else -> FormBox(result, Color.LightGray)
            }
        }
    }
}

@Composable
fun FormBox(result: String, color: Color) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(text = result, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
