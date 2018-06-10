package com.example.milu.listview1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.getString
import com.example.milu.listview1.R.string.*
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View

// https://www.raywenderlich.com/186976/android-listview-tutorial-2
// https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
class MainActivity : AppCompatActivity() {
    companion object {
        /*
        val DAYS1 = arrayOf(
                resources.getString(R.string.sunday),
            getString(monday),
            getString(tuesday),
            getString(wednesday),
            getString(thursday),
            getString(friday),
            getString(saturday)
        )
        */

        val DAYS2 = arrayOf(
                "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = resources.getString(R.string.sunday)
        val days3 = arrayOf(
                resources.getString(R.string.sunday),
                resources.getString(R.string.monday),
                resources.getString(R.string.tuesday),
                resources.getString(R.string.wednesday),
                resources.getString(R.string.thursday),
                resources.getString(R.string.friday),
                resources.getString(R.string.saturday)
        )

        val header = layoutInflater.inflate(R.layout.list_header_item,null)
        listView1.addHeaderView(header)

        //val adapter = ArrayAdapter( this, android.R.layout.simple_list_item_1, DAYS2 )
        val adapter = ArrayAdapter( this, android.R.layout.simple_list_item_1, days3 )
        listView1.adapter = adapter
    }
}
