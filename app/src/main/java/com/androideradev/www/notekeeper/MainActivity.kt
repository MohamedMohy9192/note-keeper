package com.androideradev.www.notekeeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.androideradev.www.notekeeper.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

const val USER_ID_EXTRA = "USER_ID_EXTRA"
private val LOG_TAG = MainActivity::class.simpleName

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val launchNotesActivity = Intent(this, NoteListActivity::class.java)
            launchNotesActivity.putExtra(USER_ID_EXTRA, user.uid)
            Log.i(LOG_TAG, "User Id ${user.uid}")
            startActivity(launchNotesActivity)
            finish()
        }


        binding.signInButton.setOnClickListener {
            // Choose authentication providers
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
            )

            // Create and launch sign-in intent
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(!BuildConfig.DEBUG, true)
                .build()
            signInLauncher.launch(signInIntent)
        }

    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            val launchNotesActivity = Intent(this, NoteListActivity::class.java)
            launchNotesActivity.putExtra(USER_ID_EXTRA, user?.uid)
            Log.i(LOG_TAG, user?.uid ?: "User Is Null")
            startActivity(launchNotesActivity)
            finish()
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            Log.e(LOG_TAG, "sign In Failed", response?.error)
            Log.e(LOG_TAG, "sign In Failed ${response?.error?.errorCode}")
            // ...
        }
    }
}