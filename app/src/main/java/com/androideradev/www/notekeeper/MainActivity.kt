package com.androideradev.www.notekeeper

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.androideradev.www.notekeeper.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import java.util.*

const val USER_ID_EXTRA = "USER_ID_EXTRA"
const val SIGN_IN_MESSAGE_EXTRA = "SIGN_IN_MESSAGE_EXTRA"
private val LOG_TAG = MainActivity::class.simpleName

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var referredFromActivityRequireSignIn = false

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


        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user != null && !user.isAnonymous) {
            val launchNotesActivity = Intent(this, NoteListActivity::class.java)
            launchNotesActivity.putExtra(USER_ID_EXTRA, user.uid)
            Log.i(LOG_TAG, "User Id ${user.uid}")
            startActivity(launchNotesActivity)
            finish()
        }

        val countries = listOf<String>("EG", "IQ", "LY", "OM", "US")

        binding.signInButton.setOnClickListener {
            // Choose authentication providers
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.FacebookBuilder().build(),
                AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso("EG")
                    .setWhitelistedCountries(countries)
                    .build()
            )

            // Create and launch sign-in intent
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(!BuildConfig.DEBUG, true)
                .enableAnonymousUsersAutoUpgrade()
                .setTheme(R.style.SignInTheme)
                .build()
            signInLauncher.launch(signInIntent)
        }

        /*
        * If a user already have an existing account sign in anonymously and then tries
        * to sign in with their existing account firebase will not able to handle this automatically
        * firebase will throw account merge conflict exception
        * */

        // If the user try to use feature that require sign in
        // Start MainActivity to show the sign in options and pass text message with intent to
        // demonstrate the need to sign in and display this message in main activity and hide the
        // skip sigh in button because sign in is required to use this feature
        if (intent.hasExtra(SIGN_IN_MESSAGE_EXTRA)) {
            binding.skipSighInButton.visibility = View.INVISIBLE
            binding.signInMessageTv.text = intent.getStringExtra(SIGN_IN_MESSAGE_EXTRA)
            //Intent come from activity require sign in
            referredFromActivityRequireSignIn = true
        } else {
            binding.skipSighInButton.setOnClickListener {
                auth.signInAnonymously().addOnCompleteListener { task ->
                    if (task.isComplete) {
                        launchNotesActivity()
                        finish()
                    } else {
                        Log.e(LOG_TAG, "Anonymous sign-in failed", task.exception)
                        Toast.makeText(this@MainActivity, "Sign-in failed", Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            }

        }
    }

    override fun onBackPressed() {
        //if the user try to use a feature require sign in and start the MainActivity
        // to select one of the sign in options then aborted to not select one of the option and
        // click the back button we signal to the original activity who start the sign in activity
        // that the sgin in was aborted and not to show the feature
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            if (referredFromActivityRequireSignIn) {
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                launchNotesActivity()
                finish()
            }

            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            /*
    * If a user already have an existing account and sign in anonymously and then tries
    * to sign in with their existing account firebase will not able to handle this automatically
    * firebase will throw account merge conflict exception
    * */
            if (response != null && response.error != null &&
                response.error?.errorCode == ErrorCodes.ANONYMOUS_UPGRADE_MERGE_CONFLICT
            ) {
                // this account is the object that we will merge the anonymous data into and
                // sign in to
                val fullCredential = response.credentialForLinking
                if (fullCredential != null) {
                    val auth = FirebaseAuth.getInstance()
                    auth.signInWithCredential(fullCredential)
                        .addOnSuccessListener {
                            //Check Intent come from activity require sign in
                            if (referredFromActivityRequireSignIn) {
                                val user = auth.currentUser
                                val intentData = Intent()
                                intent.putExtra(USER_ID_EXTRA, user?.uid)
                                setResult(Activity.RESULT_OK, intentData)
                                finish()
                            } else {
                                launchNotesActivity()
                                finish()
                            }

                        }
                }
            }

            //Show message to notify user when sign in failed not if user canceled or successful log in
            if (result.resultCode != Activity.RESULT_CANCELED && result.resultCode == Activity.RESULT_OK){
                Log.e(LOG_TAG, "sign In Failed", response?.error)
                Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun launchNotesActivity() {
        val user = FirebaseAuth.getInstance().currentUser
        val launchNotesActivity = Intent(this, NoteListActivity::class.java)
        launchNotesActivity.putExtra(USER_ID_EXTRA, user?.uid)
        Log.i(LOG_TAG, user?.uid ?: "User Is Null")
        Log.i(LOG_TAG, user?.email ?: "User email Is Null")
        startActivity(launchNotesActivity)
    }
}