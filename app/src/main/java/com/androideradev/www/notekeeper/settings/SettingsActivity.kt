package com.androideradev.www.notekeeper.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import com.androideradev.www.notekeeper.R
import com.androideradev.www.notekeeper.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var binding: ActivitySettingsBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, appBarConfiguration)

        // Read Preference value within an Activity
        // Step 1: Get reference to the SharedPreferences (XML File)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        // Step 2: Get the 'value' using the 'key'
        val noteLevel = sharedPreferences.getString(getString(R.string.settings_note_level_key), "")
        Log.i("SettingsActivity", "Note Level: $noteLevel")
    }

    // Called only 'after' the Preference value has changed
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        if (key == getString(R.string.settings_status_key)) {
            val newStatus = sharedPreferences?.getString(key, "")
            Toast.makeText(this, "New Status: $newStatus", Toast.LENGTH_SHORT).show()
        }

        if (key == getString(R.string.settings_auto_download_key)) {

            val autoDownload = sharedPreferences?.getBoolean(key, false)
            if (autoDownload!!) {
                Toast.makeText(this, "Auto Download: ON", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Auto Download: OFF", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }
}