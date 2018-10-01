package br.ufpe.cin.if710.rss.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.support.v7.app.AppCompatActivity
import br.ufpe.cin.if710.rss.R

class PreferenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference)
    }

    class PreferenceFrag:PreferenceFragment(){
        private var mListener: SharedPreferences.OnSharedPreferenceChangeListener? = null
        private var mUserNamePreference: Preference? = null
        companion object {
            protected val TAG = "PrefsFragment"
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferences)
        }
    }
}
