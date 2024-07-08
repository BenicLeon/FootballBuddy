package com.example.footballbuddy_final

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun UserScreen(user: FirebaseUser, navController: NavController) {
    var clubName by remember { mutableStateOf("") }


    val db = FirebaseFirestore.getInstance()


    LaunchedEffect(key1 = true) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val docRef = db.collection("clubs").document(userId)
            docRef.get()
                .addOnSuccessListener { document ->
                    val clubNameFromFirestore = document.getString("clubName")
                    if (clubNameFromFirestore != null) {
                        clubName = clubNameFromFirestore
                    }
                }
                .addOnFailureListener { e ->

                    Log.e("UserScreen", "Error retrieving club name: $e")
                }
        }
    }

    var newClubName by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val isClubAdded = clubName.isNotBlank()
    val context = LocalContext.current
    var selectedClubName by remember { mutableStateOf<String?>(null) }

    ScreenBackground {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Top
        ) {
            Spacer(modifier = Modifier.weight(1.2f))
            IconButton(onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("main_screen")
            },
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
                    .background(Color.Red, shape = RoundedCornerShape(10.dp)),

            )
            {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Sign out",tint = Color.White)
            }
        }

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newClubName,
                onValueChange = { newClubName = it },
                label = { Text("Enter your favourite club", color = Color.White, fontWeight = FontWeight.Bold) },
                textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = Color.White,
                )
            )

            Button(
                onClick = { showDialog = true },
                modifier = Modifier.size(50.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.Transparent
                )
            ) {
                Text("?")
            }
        }


        Spacer(modifier = Modifier.height(20.dp))





        Button(
            onClick = {
                if (isValidClubName(newClubName) && newClubName.isNotBlank()) {


                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        val userId = currentUser.uid
                        val docRef = db.collection("clubs").document(userId)
                        docRef.set(mapOf("clubName" to newClubName))
                            .addOnSuccessListener {
                                clubName = newClubName
                                newClubName = ""
                            }
                            .addOnFailureListener { e ->

                                Log.e("UserScreen", "Error adding club name: $e")
                            }
                    }
                }
                else {
                    Toast.makeText(context, "Club is not available or is empty", Toast.LENGTH_SHORT).show()
                    showDialog = true
                }
            },
            modifier = Modifier
                .width(110.dp)
                .height(50.dp)
                .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            enabled = !isClubAdded
        ) {
            Text(text = "Add club", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))


        Button(
            onClick = {
                if (isValidClubName(newClubName) && newClubName.isNotBlank() ) {


                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        val userId = currentUser.uid
                        val docRef = db.collection("clubs").document(userId)
                        docRef.update("clubName", newClubName)
                            .addOnSuccessListener {
                                clubName = newClubName
                                newClubName = ""
                            }
                            .addOnFailureListener { e ->

                                Log.e("UserScreen", "Error updating club name: $e")
                            }
                    }
                }
                else {
                    Toast.makeText(context, "Club is not available or is empty", Toast.LENGTH_SHORT).show()
                    showDialog = true
                }
            },
            modifier = Modifier
                .width(110.dp)
                .height(50.dp)
                .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            enabled = isClubAdded
        ) {
            Text(text = "Edit club", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(50.dp))

        if (isClubAdded) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                androidx.compose.foundation.Image(
                    painter = rememberAsyncImagePainter(model = "https://media.api-sports.io/football/teams/${getTeamIdByName(clubName)}.png"),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = clubName,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = TextStyle(fontSize = 24.sp)
                )
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
                            text = "Available Clubs",
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(fontSize = 18.sp),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        LazyColumn(modifier = Modifier.height(400.dp)) {
                            items(teams.toList()) { (teamId, teamName) ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(vertical = 4.dp)
                                        .clickable {
                                            selectedClubName =
                                                teamName
                                            newClubName =
                                                teamName
                                            showDialog = false
                                        }
                                ) {
                                    androidx.compose.foundation.Image(
                                        painter = rememberAsyncImagePainter(
                                            model = "https://media.api-sports.io/football/teams/$teamId.png"
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = teamName)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier.align(Alignment.End),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor =Color.White

                            )
                        ) {
                            Text("Close")
                        }
                    }
                }
            }
        }





    }
    BottomNavigationBar(
        navController = navController,
        onMatchesClick = { navController.navigate("fixtures_screen/${getTeamIdByName(clubName)}") },
        onNewsClick = { navController.navigate("news_screen") },
        onStatisticsClick = { navController.navigate("statistics_screen/${getTeamIdByName(clubName)}") }
    )
}
val teams = mapOf(
    554 to "Anderlecht",
    569 to "Club Brugge KV",
    631 to "Gent",
    740 to "Antwerp",
    742 to "Genk",
    561 to "HNK Rijeka",
    608 to "HNK Hajduk Split",
    616 to "NK Osijek",
    620 to "Dinamo Zagreb",
    33 to "Manchester United",
    34 to "Newcastle",
    35 to "Bournemouth",
    36 to "Fulham",
    38 to "Watford",
    39 to "Wolves",
    40 to "Liverpool",
    41 to "Southampton",
    42 to "Arsenal",
    47 to "Tottenham",
    49 to "Chelsea",
    50 to "Manchester City",
    79 to "Lille",
    81 to "Marseille",
    2 to "France",
    26 to "Argentina",
    1	to "Belgium",
    3	to "Croatia",
    10	to "England",
    25	to "Germany",
    27	to "Portugal",
    9	to "Spain",
    6	to "Brazil",
    77 to "Angers",
    78 to "Bordeaux",
    80 to "Lyon",
    82 to "Montpellier",
    83 to "Nantes",
    84 to "Nice",
    85 to "Paris Saint Germain",
    91 to "Monaco",
    157 to "Bayern Munich",
    165 to "Borussia Dortmund",
    168 to "Bayer Leverkusen",
    173 to "RB Leipzig",
    487 to "Lazio",
    489 to "AC Milan",
    492 to "Napoli",
    496 to "Juventus",
    497 to "AS Roma",
    499 to "Atalanta",
    505 to "Inter",
    194 to "Ajax",
    211 to "Benfica",
    217 to "SC Braga",
    228 to "Sporting CP",
    212 to "FC Porto",
    529 to "Barcelona",
    530 to "Atletico Madrid",
    536 to "Sevilla",
    541 to "Real Madrid",
    547 to "Girona",
    549 to "Besiktas",
    611 to "Fenerbahce",
    645 to "Galatasaray"
)

fun isValidClubName(name: String): Boolean {
    return teams.containsValue(name)
}
fun getTeamIdByName(name: String): Int? {
    return teams.entries.find { it.value == name }?.key
}