package com.example.footballbuddy_final

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.footballbuddy_final.network.Article
import com.example.footballbuddy_final.viewmodel.NewsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun NewsScreen(navController: NavController) {
    val viewModel: NewsViewModel = viewModel()
    val articles by viewModel.articles.observeAsState(emptyList())
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    var clubName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }


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
                        viewModel.fetchNews("$clubName football") // Fetch news based on club name
                        Log.d("NewsScreen", "Club name fetched: $clubName")
                    }
                }
                .addOnFailureListener { e ->
                    errorMessage = "Error retrieving club name: ${e.message}"
                    Log.e("NewsScreen", "Error retrieving club name: $e")
                }
        }
    }

    val filteredArticles = remember(articles) {
        articles.filter { article ->

            !article.title.contains("Removed", ignoreCase = true)
        }
    }

    if (filteredArticles.isEmpty()) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(errorMessage.ifBlank { "No news articles available" })
        }
    } else {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color =  Color.Black
        ) {
            LazyColumn {
                items(filteredArticles) { article ->
                    NewsItem(article, context)
                }
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        Spacer(modifier = Modifier.weight(1.2f))
        IconButton(onClick = {
            navController.navigate("user_screen")
        },
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
                .background(Color.Red, shape = RoundedCornerShape(10.dp)),

        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Navigate to user screen", tint = Color.White)
        }
    }
}

@Composable
fun NewsItem(article: Article, context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(16.dp))
            ,
        shape = RoundedCornerShape(16.dp), // Rounded corners

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            article.urlToImage?.let {
                androidx.compose.foundation.Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(bottom = 8.dp)
                )
            }
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = article.author ?: "Unknown author",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = article.publishedAt,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
            Text(
                text = article.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = article.content ?: "",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = "Read more",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Blue,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable {

                        if (article.url.isNotEmpty()) {

                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                                context.startActivity(intent)
                            } catch (e: Exception) {

                                e.printStackTrace()
                            }
                        }
                    }
            )
        }
    }
}