package milu.kiriu2010.exdb1.draw


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import milu.kiriu2010.exdb1.R

/**
 * A simple [Fragment] subclass.
 * Use the [DrawHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DrawHomeFragment : Fragment() {

    // 飾りつけされたテキストビュー
    private lateinit var decorateView1: DecorateView
    private lateinit var decorateTextView2: DecorateTextView
    private lateinit var decorateTextView3: DecorateTextView

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
        decorateView1 = view.findViewById(R.id.decrateView1)
        decorateView1.text = "あいうえお"

        decorateTextView2 = view.findViewById(R.id.decrateTextView2)
        decorateTextView2.text = "かきくけこ"

        decorateTextView3 = view.findViewById(R.id.decrateTextView3)
        decorateTextView3.text = "さしすせそ"
        decorateTextView3.mode = 1

        // アニメON/OFF
        val btnOnOff = view.findViewById<Button>(R.id.btnOnOff)
        btnOnOff.text = "OFF"
        btnOnOff.setOnClickListener {

            btnOnOff.text = if ( btnOnOff.text == "OFF" ) {
                decorateTextView2.kickRunnable(false)
                decorateTextView3.kickRunnable(false)
                "ON"
            }
            else {
                decorateTextView2.kickRunnable(true)
                decorateTextView3.kickRunnable(true)
                "OFF"
            }
        }

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
