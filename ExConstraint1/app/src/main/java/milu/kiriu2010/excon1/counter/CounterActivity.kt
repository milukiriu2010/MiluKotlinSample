package milu.kiriu2010.excon1.counter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import milu.kiriu2010.excon1.R

class CounterActivity : AppCompatActivity(), ButtonFragment.OnButtonClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        if ( savedInstanceState == null ) {

            // 動的にフラグメントを追加する
            if (supportFragmentManager.findFragmentByTag("labelFragment") == null) {
                // FragmentTransactionのインスタンスを取得
                supportFragmentManager.beginTransaction()
                        // Counter用のフラグメントを生成&追加し、counterに"0"を設定する
                        //.add(R.id.containerFragment, newLabelFragment(0), "labelFragment")
                        .replace(R.id.frameLabel, newLabelFragment(0), "labelFragment")
                        // 貼り付けを実行
                        .commit()
            }
        }
    }

    override fun onButtonClicked() {
        val fragment = supportFragmentManager.findFragmentByTag("labelFragment") as LabelFragment
        fragment.update()
    }
}
