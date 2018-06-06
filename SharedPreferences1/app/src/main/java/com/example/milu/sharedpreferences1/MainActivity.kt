package com.example.milu.sharedpreferences1

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

// https://www.simplifiedcoding.net/android-sharedpreferences-example/
// http://android.techblog.jp/archives/7836646.html
// https://www.journaldev.com/9412/android-shared-preferences-example-tutorial
class MainActivity : AppCompatActivity() {
    // https://medium.com/@ansujain/kotlin-how-to-create-static-members-for-class-543d0f126f7c
    companion object{
        val SHARED_PREF_NAME : String = "mysharedpref"
        val KEY_NAME : String = "keyname"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun saveName() {
        val name = editText.text.toString()

        if (name.isEmpty()) {
            editText.setError("Name required")
            editText.requestFocus()
            return
        }

        val sp: SharedPreferences = this.getSharedPreferences(com.example.milu.sharedpreferences1.MainActivity.SHARED_PREF_NAME, com.example.milu.sharedpreferences1.MainActivity.KEY_NAME )
        val editor: SharedPreferences.Editor = sp.edit();

        editor.putString(KEY_NAME, name)
        editor.apply()

        editText.text = ""
    }
}
