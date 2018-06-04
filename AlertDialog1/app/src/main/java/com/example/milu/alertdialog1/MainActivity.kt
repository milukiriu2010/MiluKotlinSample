package com.example.milu.alertdialog1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // https://qiita.com/superman9387/items/59e3cdc1493fbbaeac64
        button.setOnClickListener{
            AlertDialog.Builder(this)
                    .setTitle("Alert Dialog OK")
                    .setMessage("OKボタンをクリックしてください")
                    .setIcon( R.drawable.ic_launcher_foreground )
                    .setPositiveButton( "OK" ){
                        dialog, which ->
                        textView.text = "OKボタンがクリックされました"
                    }
                    .show()
        }
    }
}
