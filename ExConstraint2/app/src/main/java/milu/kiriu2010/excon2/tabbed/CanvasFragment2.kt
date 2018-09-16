package milu.kiriu2010.excon2.tabbed


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.canvas.CanvasBasicView

/**
 * A simple [Fragment] subclass.
 * Use the [CanvasFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CanvasFragment2 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_canvas1, container, false)

        val canvasBasicView1 = view.findViewById<CanvasBasicView>(R.id.canvasBasicView1)
        canvasBasicView1.mode = 7

        // 下(0->1000)へ落ちるアニメーション
        val testAnimation = TestAnimation(canvasBasicView1,0, 1000)

        // アニメーションの起動期間を設定
        testAnimation.duration = 1000
        testAnimation.repeatCount = -1
        canvasBasicView1.animation = testAnimation

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CanvasFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                CanvasFragment2().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
