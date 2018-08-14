package com.example.milu.excon1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActionBarDrawerToggle
import com.example.milu.intent2.R

class MemoActivity : AppCompatActivity() {
    // ナビゲーションドロワーの状態操作用オブジェクト
    private var drawerToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)
    }
}
