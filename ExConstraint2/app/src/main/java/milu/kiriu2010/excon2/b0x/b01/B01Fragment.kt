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

        // -------------------------------------------------------------
        // チェックボックス
        // -------------------------------------------------------------
        // 特に実装する必要なし

        // -------------------------------------------------------------
        // エディットテキスト
        // -------------------------------------------------------------
        val etB18 = findPreference<EditTextPreference>("B18_ED_KEY")
        // 共有プリファレンスの値
        val etB18Val = sp.getString("B18_ED_KEY","")
        // エディットテキストのサマリの中身
        etB18?.summary = resources.getString(R.string.B18_ED_SUMMARY) + "\n" + etB18Val.toString()

        // -------------------------------------------------------------
        // リスト
        // -------------------------------------------------------------
        val ltB18 = findPreference<ListPreference>("B18_LIST_KEY")
        // 共有プリファレンスの値
        // リストの場合、entryvaluesが保存されるので、和名に変換する必要がある
        val ltB18Val = sp.getString("B18_LIST_KEY","")
        ltB18?.let {
            // 値に対応するインデックスを取得
            val ltB18ID = it.findIndexOfValue(ltB18Val)
            if ( ltB18ID >= 0 ) {
                // インデックスから和名を求める
                val ltB18Name = it.entries[ltB18ID]
                // サマリに、和名を付与する
                it.summary = resources.getString(R.string.B18_LS_SUMMARY) + "\n" + ltB18Name.toString()
            }
        }

        // -------------------------------------------------------------
        // スイッチ
        // -------------------------------------------------------------
        // 特に実装する必要なし

        // -------------------------------------------------------------
        // シークバー
        // -------------------------------------------------------------
        val sbB18 = findPreference<Preference>("B18_SB_KEY")
        Log.d(javaClass.simpleName,"seekBar[${sbB18}]")
        // 共有プリファレンスの値
        val sbB18Val = sp.getInt("B18_SB_KEY",2)
        // シークバーのサマリの中身
        sbB18?.summary = resources.getString(R.string.B18_SB_SUMMARY) + "\n" + sbB18Val.toString()

        // シークバーの現在位置を変更すると、呼び出される
        // 変更された後、呼び出されるっぽいので、あんま意味ない
        sbB18?.setOnPreferenceChangeListener { preference, newVal ->
            Log.d(javaClass.simpleName,"preferenceChanged[${preference.javaClass.name}]")
            if (newVal is Int) {
                // シークバーのサマリの中身
                sbB18.summary = resources.getString(R.string.B18_SB_SUMMARY) + "\n" + newVal.toString()
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
        // チェックボックスに変更があった場合
        if (preference is CheckBoxPreference) {
            val bl = sharedPreferences?.getBoolean(key,false) ?: false
            //preference.setSummary(bl.toString())
            Log.d(javaClass.simpleName, "changed:checkbox:key[$key][${bl}]")
        }
        // エディットボックスに変更があった場合
        else if (preference is EditTextPreference){
            Log.d(javaClass.simpleName, "changed:edit:key[$key][${sharedPreferences?.getString(key,"")}]")
            // 共有プリファレンスのエディットテキストの値
            val etB18Val = sharedPreferences?.getString(key,"")
            // エディットテキストのサマリの中身
            preference.summary = resources.getString(R.string.B18_ED_SUMMARY) + "\n" + etB18Val.toString()
            //preference.setSummary(sharedPreferences?.getString(key,""))
        }
        // リストに変更があった場合
        else if (preference is ListPreference) {
            // 共有プリファレンスのリストの値
            // リストの場合、entryvaluesが保存されるので、和名に変換する必要がある
            val ltB18Val = sharedPreferences?.getString(key,"")
            // 値に対応するインデックスを取得
            val ltB18ID = preference.findIndexOfValue(ltB18Val)
            Log.d(javaClass.simpleName, "changed:key[$key]prefIndex[$ltB18ID]")
            if (ltB18ID >= 0) {
                // インデックスから和名を求める
                val ltB18Name = preference.entries[ltB18ID]
                // サマリに、和名を付与する
                preference.summary = resources.getString(R.string.B18_LS_SUMMARY) +
                        "\n" +
                        ltB18Name.toString()
            }
        }
        // スイッチに変更があった場合
        else if (preference is SwitchPreference) {
            val bl = sharedPreferences?.getBoolean(key,false) ?: false
            //preference.setSummary(bl.toString())
            Log.d(javaClass.simpleName, "changed:switch:key[$key][${bl}]")
        }
        // シークバーに変更があった場合
        else if (preference is SeekBarPreference) {
            val num = sharedPreferences?.getInt(key,2) ?: 2
            preference.setSummary(num.toString())
            // シークバーのサマリの中身
            preference.summary = resources.getString(R.string.B18_SB_SUMMARY) + "\n" + num
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
