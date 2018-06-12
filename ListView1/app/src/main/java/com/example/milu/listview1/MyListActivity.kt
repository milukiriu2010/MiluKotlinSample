package com.example.milu.listview1

import android.app.ListActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

// http://www.vogella.com/tutorials/AndroidListView/article.html#listsactivity_layout
open class MyListActivity : ListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list)

        val str: String = intent.getStringExtra("aaa" )

        val values = arrayOf( "Android", "iPhone", "WindowsMobile", "Blackberry", "WebOS",
                "Ubuntu", "Windows7", "Mac OS X", "Linux", "OS/2" )
        val adapter = ArrayAdapter( this, R.layout.activity_my_list, values )
        this.listAdapter = adapter
    }

    protected fun onListItemClick(l: ListView, v: View, position: Integer, id: Long ) {
        val item = this.listAdapter.getItem(position.toInt())
        Toast.makeText( this, item.toString() + " selected", Toast.LENGTH_LONG ).show()
    }
}
