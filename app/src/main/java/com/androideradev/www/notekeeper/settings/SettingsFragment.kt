package com.androideradev.www.notekeeper.settings

import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.*
import com.androideradev.www.notekeeper.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val accountSettingsPreference =
            findPreference<Preference>(getString(R.string.settings_account_settings_key))

        accountSettingsPreference?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                val navHostFragment =
                    activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController
                navController.navigate(R.id.action_settingsFragment_to_accountSettingsFragment)
                true
            }

        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)

        val statusPreference =
            findPreference<EditTextPreference>(getString(R.string.settings_status_key))

        statusPreference?.setOnPreferenceChangeListener { preference, newValue ->

            val newStatusValue = newValue as String
            if (newStatusValue.contains("bad")) {
                Toast.makeText(
                    context,
                    "Inappropriate Status, Please maintain community guidelines.",
                    Toast.LENGTH_SHORT
                )
                    .show()
                false
            } else {
                // true Accept the new preference value and update the  value
                // in the SharedPreference file or false to reject the new value and keep the old value in
                //  the SharedPreference  as the same
                true
            }


        }

        val notificationPref =
            findPreference<SwitchPreferenceCompat>(getString(R.string.settings_new_course_notification_key))
        notificationPref?.summaryProvider =
            Preference.SummaryProvider<SwitchPreferenceCompat> { switchPreference ->

                if (switchPreference.isChecked) {
                    "Status: ON"
                } else {
                    "Status: OFF"
                }

            }
    }


}

