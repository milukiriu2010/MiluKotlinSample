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
import kotlinx.android.synthetic.main.fragment_a22_pager.view.*

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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_a22, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // 前画面に戻る
            android.R.id.home -> {
                finish()
                true
            }
            // Setting
            R.id.item_a22a -> {
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    // 各ページを表示するフラグメント
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_a22_pager, container, false)
            rootView.tvA22.text = getString(R.string.TV_A22, arguments?.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        companion object {
            // セクション番号(ページ番号)を渡すキー
            private val ARG_SECTION_NUMBER = "section_number"

            // セクション番号をフラグメントに渡す
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
