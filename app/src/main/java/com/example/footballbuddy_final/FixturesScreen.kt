package com.example.footballbuddy_final

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.footballbuddy_final.network.Fixture
import com.example.footballbuddy_final.viewmodel.FixturesViewModel

@Composable
fun FixturesScreen(navController: NavController, teamId: Int, fixturesViewModel: FixturesViewModel = viewModel()) {
    var numberOfMatches by remember { mutableIntStateOf(15) }
    var isNext by remember { mutableStateOf(true) }
    val apiBASEURL = "https://v3.football.api-sports.io/"
    val userAPIKEY = "1ead3604a17f7d9186490de5a854af3e"
    val fixturesResponse by fixturesViewModel.fixtures.observeAsState()

    LaunchedEffect(Unit) {
        fixturesViewModel.fetchFixtures(apiBASEURL, userAPIKEY, teamId, numberOfMatches, isNext)
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
                value = numberOfMatches.toString(),
                onValueChange = { numberOfMatches = it.toIntOrNull() ?: numberOfMatches },
                label = { Text("Number of matches") },
                textStyle = TextStyle(color = Color.Black),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    fixturesViewModel.fetchFixtures(apiBASEURL, userAPIKEY, teamId, numberOfMatches, isNext)
                },
                modifier = Modifier.padding(start = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )

            ) {
                Text("Submit")
            }
        }

        // Buttons for Next and Last
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    isNext = true
                    fixturesViewModel.fetchFixtures(apiBASEURL, userAPIKEY, teamId, numberOfMatches, isNext)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .border(
                        width = 1.dp,
                        color = if (!isNext) Color.Black else Color.White,
                        shape = RoundedCornerShape(16.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isNext) Color.Black else Color.White,
                    contentColor = if (!isNext) Color.Black else Color.White
                )
            ) {
                Text("Next")
            }

            Button(
                onClick = {
                    isNext = false
                    fixturesViewModel.fetchFixtures(apiBASEURL, userAPIKEY, teamId, numberOfMatches, isNext)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .border(
                        width = 1.dp,
                        color = if (!isNext) Color.White else Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isNext) Color.Black else Color.White,
                    contentColor = if (!isNext) Color.White else Color.Black,

                )
            ) {
                Text(
                    text = "Last"
                )
            }
        }

        // Spacer to push content down
        Spacer(modifier = Modifier.height(16.dp))

        // Display matches or loading message
        if (fixturesResponse == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading matches...")
            }
        } else {
            if (fixturesResponse!!.isEmpty()) {
                Text("No matches available")
            } else {
                LazyColumn {
                    items(fixturesResponse!!) { fixture ->
                        FixtureItem(fixture, isNext)
                    }
                }
            }
        }
    }
}
@Composable
fun FixtureItem(fixture: Fixture, isNext: Boolean) {
    val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
    val fixtureDate = dateFormat.format(java.util.Date(fixture.fixture.timestamp * 1000))
    val fixtureTime = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date(fixture.fixture.timestamp * 1000))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                androidx.compose.foundation.Image(
                    painter = rememberAsyncImagePainter(model = fixture.teams.home.logo),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = fixtureDate,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = fixtureTime,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                androidx.compose.foundation.Image(
                    painter = rememberAsyncImagePainter(model = fixture.teams.away.logo),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = fixture.teams.home.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(3f)
                )
                Text(
                    text = if (isNext) "vs" else "${fixture.goals.home ?: 0} : ${fixture.goals.away ?: 0}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(2f).padding(horizontal = 8.dp)
                )
                Text(
                    text = fixture.teams.away.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(5f)
                                     .padding(horizontal = 16.dp)
                )
            }
        }
    }
}