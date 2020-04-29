package milu.kiriu2010.excon2.screen2.conf


import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.SwitchPreference
import androidx.preference.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.excon2.R

class ConfFragment : PreferenceFragmentCompat()
        , SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.conf_pref, rootKey)
        sp = PreferenceManager.getDefaultSharedPreferences(activity)
        //onSharedPreferenceChanged(sp,"list_key")

        val seekBar = findPreference("seekbar_key")
        Log.d(javaClass.simpleName,"seekBar[${seekBar}]")
        // こない
        seekBar?.setOnPreferenceChangeListener { preference, newVal ->
            Log.d(javaClass.simpleName,"preferenceChanged[${preference.javaClass.name}]")
            if (newVal is Integer) {
                seekBar.setSummary(newVal.toString())
                true
            }
            else {
                false
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val preference = findPreference(key)
        Log.d(javaClass.simpleName,"preference[${preference.javaClass.name}]")
        if (preference is ListPreference) {
            val prefIndex = preference.findIndexOfValue(sharedPreferences?.getString(key,""))
            Log.d(javaClass.simpleName, "changed:key[$key]prefIndex[$prefIndex]")
            if (prefIndex >= 0) {
                preference.setSummary(preference.entries[prefIndex])
            }
        }
        else if (preference is CheckBoxPreference) {
            val bl = sharedPreferences?.getBoolean(key,false) ?: false
            preference.setSummary(bl.toString())
            Log.d(javaClass.simpleName, "changed:checkbox:key[$key][${bl}]")
        }
        else if (preference is EditTextPreference){
            Log.d(javaClass.simpleName, "changed:edit:key[$key][${sharedPreferences?.getString(key,"")}]")
            preference.setSummary(sharedPreferences?.getString(key,""))
        }
        else if (preference is SwitchPreference) {
            val bl = sharedPreferences?.getBoolean(key,false) ?: false
            preference.setSummary(bl.toString())
            Log.d(javaClass.simpleName, "changed:switch:key[$key][${bl}]")
        }
        else if (preference is SeekBarPreference) {
            val num = sharedPreferences?.getInt(key,2) ?: 2
            preference.setSummary(num.toString())
            Log.d(javaClass.simpleName, "changed:seekbar:key[$key][${num}]")
        }
        else {
            Log.d(javaClass.simpleName, "changed:else:key[$key][${sharedPreferences?.javaClass}]")
            //preference.setSummary(sharedPreferences?.getString(key,""))
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
                .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                ConfFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
