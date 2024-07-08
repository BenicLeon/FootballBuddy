package com.example.footballbuddy_final

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun LoginRegisterScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    val buttonHeight = 40.dp
    val buttonWidth = 120.dp
    val textFieldWidth = 300.dp

    ScreenWithBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.White, fontWeight = FontWeight.Bold) },
                textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .width(textFieldWidth)
                    .height(56.dp)
                    .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = Color.White,
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color.White, fontWeight = FontWeight.Bold) },
                visualTransformation = PasswordVisualTransformation(),
                textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .width(textFieldWidth)
                    .height(56.dp)
                    .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = Color.White,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { validateAndSignIn(context, email, password, navController) },
                    modifier = Modifier
                        .width(buttonWidth)
                        .height(buttonHeight)
                        .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Login", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { validateAndRegister(context, email, password) },
                    modifier = Modifier
                        .width(buttonWidth)
                        .height(buttonHeight)
                        .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Register", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private fun validateAndSignIn(context: Context, email: String, password: String, navController: NavController) {
    if (email.isEmpty() || password.isEmpty()) {
        Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
    } else {
        signIn(context, email, password, navController)
    }
}

private fun validateAndRegister(context: Context, email: String, password: String) {
    if (email.isEmpty() || password.isEmpty()) {
        Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
    } else {
        register(context, email, password)
    }
}

private fun signIn(context: Context, email: String, password: String, navController: NavController) {
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    val userId = user.uid
                    val db = FirebaseFirestore.getInstance()
                    db.collection("clubs")
                        .document(userId)
                        .get()
                        .addOnSuccessListener { document ->
                            val clubName = document?.getString("clubName")
                            if (clubName != null) {
                                Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()
                                navController.navigate("user_screen")
                            } else {

                                Toast.makeText(context, "Please enter your favorite club", Toast.LENGTH_SHORT).show()

                                navController.navigate("user_screen")
                            }
                        }
                        .addOnFailureListener { e ->

                            Toast.makeText(context, "Error fetching club name: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {

                    Toast.makeText(context, "User is null", Toast.LENGTH_SHORT).show()
                }
            } else {

                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
}



private fun register(context: Context, email: String, password: String) {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Registered successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }
}

