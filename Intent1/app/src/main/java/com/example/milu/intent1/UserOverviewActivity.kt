package com.example.milu.intent1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class UserOverviewActivity : AppCompatActivity() {
    val SUB_ACTIVITY_CREATE_USER = 10
    private val user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_overview)
    }
}
