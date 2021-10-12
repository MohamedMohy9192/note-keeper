package com.androideradev.www.notekeeper.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.androideradev.www.notekeeper.R


class AccountSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.account_settings, rootKey)
    }


}