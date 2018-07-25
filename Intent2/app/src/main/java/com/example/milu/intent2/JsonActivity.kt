package com.example.milu.intent2

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_json.*
import kotlinx.android.synthetic.main.content_json.*
import org.json.JSONObject
import java.io.File

class JsonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_json)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        btnBack.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        btnJSON.setOnClickListener() {
           txtJSON.text = this.loadJSON()
        }
    }

    private fun loadJSON() : String {
        val sb = StringBuffer()
        val istream = this.resources.openRawResource(R.raw.color)
        val reader = istream.bufferedReader()
        val iterator = reader.lineSequence().iterator()
        while ( iterator.hasNext() ) {
            sb.append( iterator.next() )
        }
        reader.close()
        istream.close()

        Log.v( this.javaClass.toString(), "=== JSON content ================" )
        Log.v( this.javaClass.toString(), sb.toString() )
        Log.v( this.javaClass.toString(), "=================================" )

        val objJSON = JSONObject(sb.toString())

        val sbColor = StringBuffer()
        val colors = objJSON.getJSONArray("colors")
        for ( i in 0 until colors.length() ){
            val color = colors.getJSONObject(i)
            sbColor.append( color.getString("color") + File.separator )
        }
        return sbColor.toString()
    }
}
