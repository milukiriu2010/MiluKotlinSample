package milu.kiriu2010.excon2.a0x.a22


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.a0x.a19.CanvasBasicView

// カスタマイズビュー(CanvasBasicView)にページ番号(mode)を渡して動作を変える
// 上から下に落ちるアニメーション
class A22CanvasFragment2 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_a22_canvas, container, false)

        val canvasBasicView1 = view.findViewById<CanvasBasicView>(R.id.cvA22)
        canvasBasicView1.mode = 7

        // 下(0->1000)へ落ちるアニメーション
        val testAnimation = A22TestAnimation(canvasBasicView1, 0, 1000)

        // アニメーションの起動期間を設定
        testAnimation.duration = 1000
        testAnimation.repeatCount = -1
        canvasBasicView1.animation = testAnimation

        return view
    }


    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                A22CanvasFragment2().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
