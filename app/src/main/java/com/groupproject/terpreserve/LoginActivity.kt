package com.groupproject.terpreserve
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    // Initialize the ActivityResultLauncher
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res -> onSignInResult(res) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // Example trigger to start the sign-in flow
        findViewById<Button>(R.id.loginButton).setOnClickListener {
            startSignIn()
        }
    }

    private fun startSignIn() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()  // Finish LoginActivity so user can't return to it with the back button
        } else {
            // Sign in failed
            if (result.idpResponse == null) {
                // User pressed back button
                finish()  // Optionally finish LoginActivity returning to MainActivity or exit
            } else {
                // Handle errors
                // e.g., show an error message
            }
        }
    }
}
