package milu.kiriu2010.excon2.a0x.a02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_a02.*

// ストップウォッチ
class A02Activity : AppCompatActivity() {

    val handler = Handler()
    // ストップウォッチの経過時間
    var timeValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a02)

        // 1秒ごとに処理を実行
        val runnable = object: Runnable{
            override fun run() {
                this@A02Activity.timeValue++
                // TextViewを更新
                // ?.letを用いて、nullではない場合のみ更新
                this@A02Activity.timeToText(timeValue)?.let {
                    tvA02.text = it
                }
                // 1秒後に再び、自分自身(Runnable)を呼び出す
                handler.postDelayed(this,1000)
            }
        }

        // ストップウォッチ：開始
        btnA02A.setOnClickListener {
            handler.post(runnable)
        }

        // ストップウォッチ：停止
        btnA02B.setOnClickListener {
            handler.removeCallbacks(runnable)
        }

        // ストップウォッチ：リセット
        btnA02C.setOnClickListener {
            handler.removeCallbacks(runnable)
            timeValue = 0
            this@A02Activity.timeToText()?.let {
                tvA02.text = it
            }
        }
    }

    // 数値を00:00:00形式の文字列に変換する関数
    // 引数timeにはデフォルト値0を設定、返却する型はnullableなString?型
    private fun timeToText(time: Int = 0): String? {
        // if式は値を返すため、そのままreturnできる
        return if (time < 0) {
            null
        } else if (time == 0) {
            "00:00:00"
        } else {
            val h = time / 3600
            val m = time % 3600 / 60
            val s = time % 60
            "%1$02d:%2$02d:%3$02d".format(h, m, s)
        }
    }

}
