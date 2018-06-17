package com.example.milu.intent2

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_user_add.*

class UserAddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_add)

        val strFirstName = intent.getStringExtra("firstName") ?: "<NULL>"
        lblFirstName.text = strFirstName
        Toast.makeText(this, strFirstName, Toast.LENGTH_LONG )
        Log.d("bXXXXXXXXXXXX:", strFirstName )

        btnBack.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
