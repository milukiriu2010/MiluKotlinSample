package com.example.milu.intent2

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.Typeface
import kotlinx.android.synthetic.main.activity_main.*
import com.example.milu.abc.Team
import com.example.milu.abc.TeamBaseBall
import com.example.milu.abc.TeamBaseBall.LEAGUE.*
import com.example.milu.abc.TeamSoccer
import com.example.milu.abc.AppConst
import kotlinx.android.synthetic.main.activity_user_add.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.setTextFirstName()

        this.setAction()
    }

    private fun setTextFirstName(){
        // https://techacademy.jp/magazine/4773
        //lblFirstName.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
        val pref: SharedPreferences = getApplicationContext().getSharedPreferences(AppConst.PREF_USER_FIRST_NAME.toString(), Context.MODE_PRIVATE)
        val firstName = pref.getString( AppConst.KEY_USER_FIRST_NAME.toString(), "" )
        txtFirstName.setText(firstName)
    }


    private fun setAction() {
        btnAddUser.setOnClickListener{
            val intent = Intent( this, UserAddActivity::class.java )
            val strFirstName = txtFirstName.text ?: "<arere>"
            Log.d(this.javaClass.name, strFirstName.toString() )
            intent.putExtra("firstName", strFirstName.toString() )

            val pref: SharedPreferences = getApplicationContext().getSharedPreferences(AppConst.PREF_USER_FIRST_NAME.toString(), Context.MODE_PRIVATE)
            val editor : Editor = pref.edit()
            editor.putString( AppConst.KEY_USER_FIRST_NAME.toString(), strFirstName.toString() )
            editor.apply()

            startActivityForResult( intent, IntentID.ID_USER_ADD.value )
        }

        // http://www.vogella.com/tutorials/AndroidIntent/article.html
        // start browser
        btnBrowse.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://sourceforge.net/projects/miludbviewer/files/?source=navbar")
            startActivity(intent)
        }

        btnImage.setOnClickListener{
            val intent = Intent( this, ImageActivity::class.java )
            startActivityForResult( intent, IntentID.ID_IMAGE.value )
        }

        btnTeamLst.setOnClickListener {
            val intent = Intent( this, TeamListActivity::class.java )
            startActivityForResult( intent, IntentID.ID_TEAM_LIST.value )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when ( requestCode ){
            IntentID.ID_USER_ADD.value  -> ""
            IntentID.ID_IMAGE.value      -> ""
            IntentID.ID_TEAM_LIST.value -> ""
        }
        //if ( resultCode == Activity.RESULT_OK )
        super.onActivityResult(requestCode, resultCode, data)
    }
}
