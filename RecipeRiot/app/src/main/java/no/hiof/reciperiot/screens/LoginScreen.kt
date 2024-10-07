package no.hiof.reciperiot.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.google.firebase.auth.FirebaseAuth
import no.hiof.reciperiot.R
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(
    onSignInClick: (String, String) -> Unit,
    showNotification: (String) -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation()
        )
        val auth = FirebaseAuth.getInstance()
        Button(onClick = {
            auth.signInWithEmailAndPassword("default@mcdefaultson.com", "default")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSignInClick("default@mcdefaultson.com", "default")
                        // Sign in successful, navigate to the main app screen.
                        showNotification("Nils") // Pass a sample user string for now
                    } else {
                        // Sign in failed, display an error message.
                        Log.e(TAG, "Error Login failed")
                    }
                }
        }) {
            Text("Test Sign In")
        }

        Button(onClick = {
            Log.d(TAG,"$email  $password")
            if (email == "" || password == "") {
                //Email or password empty
                Log.e(TAG, "Email or password empty")
            }
            else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onSignInClick(email, password)
                            //val user = Firebase.auth.currentUser
                            showNotification(email)
                            // Sign in successful, navigate to the main app screen.
                        } else {
                            // Sign in failed, display an error message.
                            Log.e(TAG, "Error Login failed")
                        }
                    }
            }
        }) {
            Text(stringResource(R.string.sign_in))
        }
    }
}