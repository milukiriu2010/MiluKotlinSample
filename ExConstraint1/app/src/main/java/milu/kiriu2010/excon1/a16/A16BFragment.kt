package milu.kiriu2010.excon1.a16

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import milu.kiriu2010.excon1.R
import java.lang.RuntimeException

// カウントアップする側のフラグメント
// ボタンを押してカウントアップする
class A16BFragment: Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if ( context !is OnButtonClickListener) {
            throw RuntimeException("リスナーを実装してください")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_a16b, container,false )
        /*
        button.setOnClickListener {
            val listener = context as? OnButtonClickListener
            listener?.onButtonClicked()
        }
        */

        view.findViewById<Button>(R.id.btnA16B)
                .setOnClickListener {
                    val listener = context as? OnButtonClickListener
                    listener?.onButtonClicked()
                }
        return view
    }

    interface OnButtonClickListener {
        fun onButtonClicked()
    }
}