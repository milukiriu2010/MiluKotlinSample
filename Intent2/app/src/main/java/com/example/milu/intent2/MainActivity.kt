package com.example.milu.intent2

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val ID_USER_ADD = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAddUser.setOnClickListener{
            val intent = Intent( this, UserAddActivity::class.java )
            val strFirstName = txtFirstName.text ?: "<arere>"
            Log.d("aXXXXXXXXXXXX:", strFirstName.toString() )
            intent.putExtra("firstName", strFirstName.toString() )

            startActivityForResult( intent, ID_USER_ADD )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ( requestCode == ID_USER_ADD ){

        }
        //if ( resultCode == Activity.RESULT_OK )
        super.onActivityResult(requestCode, resultCode, data)
    }
}
