package milu.kiriu2010.excon1.counter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import milu.kiriu2010.excon1.R
import java.lang.RuntimeException

class ButtonFragment: Fragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if ( context !is OnButtonClickListener) {
            throw RuntimeException("リスナーを実装してください")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_button, container,false )
        /*
        button.setOnClickListener {
            val listener = context as? OnButtonClickListener
            listener?.onButtonClicked()
        }
        */

        view.findViewById<Button>(R.id.button)
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