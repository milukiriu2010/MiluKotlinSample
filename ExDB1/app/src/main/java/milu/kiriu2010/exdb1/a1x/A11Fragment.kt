package milu.kiriu2010.exdb1.a1x


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import milu.kiriu2010.exdb1.R

// 影付きボタン
class A11Fragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_a11,container,false)

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() =
                A11Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
