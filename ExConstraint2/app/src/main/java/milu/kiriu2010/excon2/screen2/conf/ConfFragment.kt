package milu.kiriu2010.excon2.screen2.conf


import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.excon2.R

class ConfFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        addPreferencesFromResource(R.xml.conf_pref)
    }

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
    }

    /*
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conf, container, false)
    }
    */


    companion object {
        @JvmStatic
        fun newInstance() =
                ConfFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
