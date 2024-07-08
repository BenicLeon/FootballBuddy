package com.example.footballbuddy_final


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.footballbuddy_final.ui.theme.FootballBuddy_finalTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContent {
            FootballBuddy_finalTheme {
                val context = LocalContext.current
                val currentUser = FirebaseAuth.getInstance().currentUser
                val startDestination = if (currentUser != null) "user_screen" else "main_screen"
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = startDestination) {
                    composable("main_screen") {
                        LoginRegisterScreen(navController)
                    }
                    composable("user_screen") {
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        if (currentUser != null) {
                            UserScreen(user = currentUser, navController)
                        } else {
                            navController.navigate("main_screen") {
                                popUpTo("main_screen") { inclusive = true }
                            }
                        }
                    }
                    composable("news_screen") {
                        NewsScreen(navController = navController)
                    }
                    composable("fixtures_screen/{teamId}") { navBackStackEntry ->
                        val teamId = navBackStackEntry.arguments?.getString("teamId")?.toIntOrNull()
                        if (teamId != null) {
                            FixturesScreen(navController = navController, teamId = teamId)
                        } else {
                            Toast.makeText(context, "Club is empty", Toast.LENGTH_SHORT).show()
                        }
                    }
                    composable("statistics_screen/{teamId}") { navBackStackEntry ->
                        val teamId = navBackStackEntry.arguments?.getString("teamId")?.toIntOrNull()
                        if (teamId != null) {
                            StatisticsScreen(navController = navController, teamId = teamId)
                        } else {
                            Toast.makeText(context, "Club is empty", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun BackgroundImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.fitness),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alpha = 0.9F,
        modifier = modifier
    )
}
@Composable
fun Image(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alpha = 0.9F,
        modifier = modifier
    )
}

@Composable
fun ScreenWithBackground(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage(modifier = Modifier.fillMaxSize())
        content()
    }
}
@Composable
fun ScreenBackground(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(modifier = Modifier.fillMaxSize())
        content()
    }
}


@Composable
fun BackButton(navController: NavController) {
    IconButton(
        onClick = { navController.navigate("user_screen") },
        modifier = Modifier.padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back"
        )
    }
}