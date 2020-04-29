package milu.kiriu2010.exdb1.draw


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import milu.kiriu2010.exdb1.R
import milu.kiriu2010.gui.decorate.Deco03ConstraintLayout
import milu.kiriu2010.gui.decorate.DecorateTextView
import milu.kiriu2010.gui.decorate.DecorateView

/**
 * A simple [Fragment] subclass.
 * Use the [Draw02DecorateTextFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Draw02DecorateTextFragment : Fragment() {

    // レイアウト
    private lateinit var rootLayout: Deco03ConstraintLayout
    // 飾りつけされたテキストビュー
    private lateinit var decorateView1: DecorateView
    private lateinit var decorateTextView2: DecorateTextView
    private lateinit var decorateTextView3: DecorateTextView
    private lateinit var decorateTextView4: DecorateTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_draw02_decorate_text, container, false)

        // レイアウト
        rootLayout = view.findViewById(R.id.rootLayout)

        // 飾りつけされたテキストビュー
        decorateView1 = view.findViewById(R.id.decrateView1)
        decorateView1.text = "あいうえお"

        decorateTextView2 = view.findViewById(R.id.decrateTextView2)
        decorateTextView2.text = "かきくけこ"

        decorateTextView3 = view.findViewById(R.id.decrateTextView3)
        decorateTextView3.text = "さしすせそ"
        decorateTextView3.mode = 1

        decorateTextView4 = view.findViewById(R.id.decrateTextView4)
        decorateTextView4.text = "たちつてと"
        decorateTextView4.mode = 2

        // アニメON/OFF
        val btnOnOff = view.findViewById<Button>(R.id.btnOnOff)
        btnOnOff.text = "OFF"
        btnOnOff.setOnClickListener {

            btnOnOff.text = if ( btnOnOff.text == "OFF" ) {
                rootLayout.kickRunnable(false)
                decorateTextView2.kickRunnable(false)
                decorateTextView3.kickRunnable(false)
                decorateTextView4.kickRunnable(false)
                "ON"
            }
            else {
                rootLayout.kickRunnable(true)
                decorateTextView2.kickRunnable(true)
                decorateTextView3.kickRunnable(true)
                decorateTextView4.kickRunnable(true)
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
