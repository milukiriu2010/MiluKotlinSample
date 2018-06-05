package com.example.milu.transferactivity1

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

// http://android.techblog.jp/archives/7836434.html#more
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            // strという名前で文字を登録
            intent.putExtra("str", "ぬるぽ")
            // 第二引数は遷移先を区別するためのIDを記述する。自分で決めていい
            startActivityForResult(intent, 99)

            Log.v("めいん", "くりっく官僚")
        }
    }

    // 遷移先から戻ってきたときに発生するイベント
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode){
            Activity.RESULT_OK -> {
                val str1: String = data?.getStringExtra("str1") ?:""
                Log.v("nullpo", resultCode.toString())
                Log.v("nullpo", str1)
                textView.text = str1
            }
        }
    }
}
