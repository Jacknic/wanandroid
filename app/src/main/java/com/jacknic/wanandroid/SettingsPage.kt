package com.jacknic.wanandroid

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class SettingsPage : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.basic_settings, rootKey)

    }
}