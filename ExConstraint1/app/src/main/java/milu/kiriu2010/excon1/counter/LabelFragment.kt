package milu.kiriu2010.excon1.counter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import milu.kiriu2010.excon1.R

class LabelFragment: Fragment() {
    private var counter = 0
    private lateinit var counterLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //counter = arguments?.getInt("counter") ?: 0
        counter = savedInstanceState?.getInt("counter")
                ?: arguments?.getInt("counter")
                ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_label, container, false )
        counterLabel = view.findViewById(R.id.counterLabel)
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

fun newLabelFragment(value: Int): LabelFragment {
    val fragment = LabelFragment()

    val args = Bundle()
    args.putInt("counter",value)

    fragment.arguments = args
    return fragment
}