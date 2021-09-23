package com.androideradev.www.notekeeper.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.androideradev.www.notekeeper.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }


}

/* val navHostFragment =
                activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController

            navController.navigate(R.id.action_settingsFragment_to_accountSettingsFragment)*/