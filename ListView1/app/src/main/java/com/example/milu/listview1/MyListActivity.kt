package com.example.milu.listview1

import android.app.ListActivity
import android.os.Bundle
import android.widget.ArrayAdapter

// http://www.vogella.com/tutorials/AndroidListView/article.html#listsactivity_layout
class MyListActivity : ListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list)

        val values = arrayOf( "Android", "iPhone", "WindowsMobile", "Blackberry", "WebOS",
                "Ubuntu", "Windows7", "Mac OS X", "Linux", "OS/2" )
        val adapter = ArrayAdapter( this, R.layout.rowlayout, R.id.label, values )
        this.listAdapter = adapter
    }
}
