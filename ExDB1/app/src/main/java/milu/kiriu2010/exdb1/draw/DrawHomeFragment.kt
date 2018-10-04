package milu.kiriu2010.exdb1.draw


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.exdb1.R

/**
 * A simple [Fragment] subclass.
 * Use the [DrawHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DrawHomeFragment : Fragment() {

    // 飾りつけされたテキストビュー
    private lateinit var decorateTextView1: DecorateTextView
    private lateinit var decorateTextView2: DecorateTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_draw_home, container, false)

        // 飾りつけされたテキストビュー
        decorateTextView1 = view.findViewById(R.id.decrateTextView1)
        decorateTextView1.text = "あいうえお"

        decorateTextView2 = view.findViewById(R.id.decrateTextView2)
        decorateTextView2.text = "かきくけこ"

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                DrawHomeFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
