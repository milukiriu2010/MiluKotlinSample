package milu.kiriu2010.exdb1.b0x

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import milu.kiriu2010.exdb1.R

// 影付きボタン
class B01Fragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_b01,container,false)
        Log.d(javaClass.simpleName,"CreateView")

        return view
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
