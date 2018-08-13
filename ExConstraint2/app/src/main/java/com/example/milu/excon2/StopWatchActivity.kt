package com.example.milu.excon2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_stop_watch.*

class StopWatchActivity : AppCompatActivity() {

    val handler = Handler()
    var timeValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stop_watch)

        // 1秒ごとに処理を実行
        val runnable = object: Runnable{
            override fun run() {
                this@StopWatchActivity.timeValue++
                // TextViewを更新
                // ?.letを用いて、nullではない場合のみ更新
                this@StopWatchActivity.timeToText(timeValue)?.let {
                    txtTIME.text = it
                }
                handler.postDelayed(this,1000)
            }
        }

        btnSTART.setOnClickListener {
            handler.post(runnable)
        }

        btnSTOP.setOnClickListener {
            handler.removeCallbacks(runnable)
        }

        btnRESET.setOnClickListener {
            handler.removeCallbacks(runnable)
            timeValue = 0
            this@StopWatchActivity.timeToText()?.let {
                txtTIME.text = it
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
