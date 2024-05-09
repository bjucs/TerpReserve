package com.groupproject.terpreserve
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult


class LoginActivity : AppCompatActivity() {

    // Initialize the ActivityResultLauncher
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res -> onSignInResult(res) }
    private lateinit var sharedPreferences : SharedPreferences

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            Log.w(TAG, "Logging in")
            startSignIn()
        }

        findViewById<CheckBox>(R.id.saveLoginCheckBox).setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            if (isChecked) {
                editor.putBoolean("StayLoggedIn", true)
            }
            else {
                editor.putBoolean("StayLoggedIn", false)
            }
            editor.apply()
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
            Log.w(TAG, "Logged in successfully!")
            // Remain signed upon successful login if the box was checked
            val editor = sharedPreferences.edit()
            if (sharedPreferences.getBoolean("StayLoggedIn", false)) {
                editor.putBoolean("LoggedIn", true)
                editor.apply()
            }

            // Switch to reservation page
            val locationsIntent = Intent(this, LocationsActivity::class.java)
            startActivity(locationsIntent)
            finish()
        }
        else {
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
