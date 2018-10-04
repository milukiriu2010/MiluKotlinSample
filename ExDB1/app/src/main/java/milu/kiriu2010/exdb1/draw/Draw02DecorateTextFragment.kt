package milu.kiriu2010.exdb1.draw


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R

/**
 * A simple [Fragment] subclass.
 * Use the [Draw02DecorateTextFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Draw02DecorateTextFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_draw02_decorate_text, container, false)





        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment Draw02DecorateTextFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                Draw02DecorateTextFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
