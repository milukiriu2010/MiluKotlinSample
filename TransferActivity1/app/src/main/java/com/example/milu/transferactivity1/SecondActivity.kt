package com.example.milu.transferactivity1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.second_activity.*

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v("せこんど", "きた")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)

        // 名前strで登録された文字列を取得する
        val str: String = intent.getStringExtra("str")
        Log.v("せこんど","うけとれた？")
        Log.v("nullpo",str.toString())
        textView2.text = str

        button2.setOnClickListener {
            // インテントを作成して名前str1をデータ登録
            val intent = Intent()
            intent.putExtra("str1", "ぬるぽした")
            // okボタンなのでRESULT_OKを返す.キャンセルの場合はRESULT_CANCEL
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}