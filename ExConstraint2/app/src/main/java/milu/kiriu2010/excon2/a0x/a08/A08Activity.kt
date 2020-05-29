package milu.kiriu2010.excon2.a0x.a08

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_a08.*

// フィボナッチ数列
// アクティビティの状態が変更しても、値を保つようにしている
// http://tekeye.uk/android/examples/saving-activity-state
class A08Activity : AppCompatActivity() {

    //Stores previous Fibonacci number
    var number_state = 1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a08)

        number_state = savedInstanceState?.getLong("PREVIOUS") ?: 1L;
        tvA08B.setText( number_state.toString() )

        // 次ボタンを押下すると、フィボナッチ数列の次の数を表示
        btnA08A.setOnClickListener {
            // 前回の数字
            val previous_number = number_state
            // 今回の数字
            number_state += previous_number
            // 今回の数字でテキストを更新
            tvA08B.text = number_state.toString()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("PREVIOUS", number_state)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        number_state = savedInstanceState.getLong("PREVIOUS")
    }
}
