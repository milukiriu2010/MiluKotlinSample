package milu.kiriu2010.exdb1.b0x

import android.annotation.SuppressLint
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

// 枠アニメ
class B02Fragment : Fragment() {

    // レイアウト
    private lateinit var dclB02: Deco03ConstraintLayout
    // 飾りつけされたテキストビュー
    private lateinit var dvB02: DecorateView
    private lateinit var dtvB02A: DecorateTextView
    private lateinit var dtvB02B: DecorateTextView
    private lateinit var dtvB02C: DecorateTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_b02, container, false)

        // レイアウト
        dclB02 = view.findViewById(R.id.dclB02)

        // Viewを継承して、カスタムビューを作成
        dvB02 = view.findViewById(R.id.dvB02)
        dvB02.text = "あいうえお"

        // AppCompatTextViewを継承して、カスタムビューを作成
        dtvB02A = view.findViewById(R.id.dtvB02A)
        dtvB02A.text = "かきくけこ"

        // AppCompatTextViewを継承して、カスタムビューを作成
        dtvB02B = view.findViewById(R.id.dtvB02B)
        dtvB02B.text = "さしすせそ"
        dtvB02B.mode = 1

        // AppCompatTextViewを継承して、カスタムビューを作成
        dtvB02C = view.findViewById(R.id.dtvB02C)
        dtvB02C.text = "たちつてと"
        dtvB02C.mode = 2

        // アニメON/OFF
        val btnB02 = view.findViewById<Button>(R.id.btnB02)
        btnB02.text = "OFF"
        btnB02.setOnClickListener {

            btnB02.text = if ( btnB02.text == "OFF" ) {
                dclB02.kickRunnable(false)
                dtvB02A.kickRunnable(false)
                dtvB02B.kickRunnable(false)
                dtvB02C.kickRunnable(false)
                "ON"
            }
            else {
                dclB02.kickRunnable(true)
                dtvB02A.kickRunnable(true)
                dtvB02B.kickRunnable(true)
                dtvB02C.kickRunnable(true)
                "OFF"
            }

        }

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() =
                B02Fragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
