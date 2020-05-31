package milu.kiriu2010.excon1.a16

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import milu.kiriu2010.excon1.R

// -----------------------------------
// フラグメントの練習
// -----------------------------------
// 上:Fragment　　　:A16BFragment:ボタン
// 下:FrameLayout:A16LFragment:結果
// -----------------------------------
class A16Activity : AppCompatActivity(),
        A16BFragment.OnButtonClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a16)

        if ( savedInstanceState == null ) {

            // 動的にフラグメントを追加する
            if (supportFragmentManager.findFragmentByTag("labelFragment") == null) {
                // FragmentTransactionのインスタンスを取得
                supportFragmentManager.beginTransaction()
                        // Counter用のフラグメントを生成&追加し、counterに"0"を設定する
                        //.add(R.id.containerFragment, newLabelFragment(0), "labelFragment")
                        .replace(R.id.flA16, newLabelFragment(0), "labelFragment")
                        // 貼り付けを実行
                        .commit()
            }
        }
    }

    // OnButtonClickListener
    override fun onButtonClicked() {
        val fragment = supportFragmentManager.findFragmentByTag("labelFragment") as A16LFragment
        fragment.update()
    }
}
