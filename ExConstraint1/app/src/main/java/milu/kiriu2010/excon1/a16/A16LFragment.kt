package milu.kiriu2010.excon1.a16

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import milu.kiriu2010.excon1.R

// ---------------------------------------
// カウントアップされた結果を表示するフラグメント
// ---------------------------------------
class A16LFragment: Fragment() {
    private var counter = 0
    private lateinit var counterLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // savedInstanceStateは、フラグメントが再開したとき
        // argumentsは、フラグメントが生成されたとき
        counter = savedInstanceState?.getInt("counter")
                ?: arguments?.getInt("counter")
                ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_label, container, false )
        counterLabel = view.findViewById(R.id.tvA16)
        // ここをコメントアウトしないと、
        // ボタンを押しても, 0がずっと残る
        // しかし、コメントアウトすると、
        // 初期表示のとき、何も表示されない
        counterLabel.setText(counter.toString())
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //super.onSaveInstanceState(outState)
        outState.putInt("counter", counter)
    }

    fun update() {
        counter++
        counterLabel.setText( counter.toString() )
    }
}

fun newLabelFragment(value: Int): A16LFragment {
    val fragment = A16LFragment()

    val args = Bundle()
    args.putInt("counter",value)

    fragment.arguments = args
    return fragment
}