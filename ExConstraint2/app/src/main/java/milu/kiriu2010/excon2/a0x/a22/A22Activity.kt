package milu.kiriu2010.excon2.a0x.a22


//import android.app.Activity;
//import android.app.FragmentTransaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_a22.*

// ページャ
class A22Activity : AppCompatActivity() {

    private var pagerAdapter: A22PagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a22)

        // 各ページを表示するアダプタ
        pagerAdapter = A22PagerAdapter(supportFragmentManager)
        vpA22.adapter = pagerAdapter

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // 前画面に戻る
            android.R.id.home -> {
                finish()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
