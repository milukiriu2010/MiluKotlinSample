package com.example.milu.excon1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.milu.intent2.R

class CounterActivity : AppCompatActivity(), ButtonFragment.OnButtonClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        // 動的にフラグメントを追加する
        if ( supportFragmentManager.findFragmentByTag("labelFragment") == null ) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.containerFragment, newLabelFragment(0), "labelFragment")
                    .commit()
        }
    }

    override fun onButtonClicked() {
        val fragment = supportFragmentManager.findFragmentByTag("labelFragment") as LabelFragment
        fragment.update()
    }
}
