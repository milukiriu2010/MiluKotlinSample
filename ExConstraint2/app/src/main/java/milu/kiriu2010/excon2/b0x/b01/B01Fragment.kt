package milu.kiriu2010.excon2.b0x.b01


import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.SwitchPreference
import androidx.preference.*
import android.util.Log

import milu.kiriu2010.excon2.R

// 設定
class B01Fragment : PreferenceFragmentCompat()
        , SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.b01_pref, rootKey)
        sp = PreferenceManager.getDefaultSharedPreferences(activity)
        //onSharedPreferenceChanged(sp,"list_key")

        val seekBar = findPreference<Preference>("B18_SB_KEY")
        Log.d(javaClass.simpleName,"seekBar[${seekBar}]")
        // シークバーの現在位置を変更すると、呼び出される
        seekBar?.setOnPreferenceChangeListener { preference, newVal ->
            Log.d(javaClass.simpleName,"preferenceChanged[${preference.javaClass.name}]")
            if (newVal is Int) {
                seekBar.setSummary(newVal.toString())
                true
            }
            else {
                false
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val preference = findPreference<Preference>(key!!)
        preference?.let {
            Log.d(javaClass.simpleName,"preference[${it.javaClass.name}]")
        }
        // リストに変更があった場合
        if (preference is ListPreference) {
            val prefIndex = preference.findIndexOfValue(sharedPreferences?.getString(key,""))
            Log.d(javaClass.simpleName, "changed:key[$key]prefIndex[$prefIndex]")
            if (prefIndex >= 0) {
                preference.setSummary(preference.entries[prefIndex])
            }
        }
        // チェックボックスに変更があった場合
        else if (preference is CheckBoxPreference) {
            val bl = sharedPreferences?.getBoolean(key,false) ?: false
            preference.setSummary(bl.toString())
            Log.d(javaClass.simpleName, "changed:checkbox:key[$key][${bl}]")
        }
        // エディットボックスに変更があった場合
        else if (preference is EditTextPreference){
            Log.d(javaClass.simpleName, "changed:edit:key[$key][${sharedPreferences?.getString(key,"")}]")
            preference.setSummary(sharedPreferences?.getString(key,""))
        }
        // スイッチに変更があった場合
        else if (preference is SwitchPreference) {
            val bl = sharedPreferences?.getBoolean(key,false) ?: false
            preference.setSummary(bl.toString())
            Log.d(javaClass.simpleName, "changed:switch:key[$key][${bl}]")
        }
        // シークバーに変更があった場合
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
                B01Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
