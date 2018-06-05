package com.example.milu.optionmenu1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import android.view.MenuItem

// http://android.techblog.jp/archives/8530309.html
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerForContextMenu( layoutView )
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menuInflater.inflate( R.menu.main, menu )
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.item01 -> { Log.v("nullpo", "1"); textView.text = "1 selected." }
            R.id.item02 -> { Log.v("nullpo", "2"); textView.text = "2 selected." }
            R.id.item03 -> { Log.v("nullpo", "3"); textView.text = "3 selected." }
        }
        return super.onContextItemSelected(item)
    }
}
