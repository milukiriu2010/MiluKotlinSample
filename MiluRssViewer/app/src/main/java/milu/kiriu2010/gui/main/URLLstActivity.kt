package milu.kiriu2010.gui.main

import android.content.Intent
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.MenuItem
import milu.kiriu2010.entity.URLData
import milu.kiriu2010.gui.each.RssEachActivity
import milu.kiriu2010.id.IntentID
import milu.kiriu2010.milurssviewer.R

class URLLstActivity : AppCompatActivity(),
    URLLstFragment.OnURLSelectListener {

    // ナビゲーションドロワーの状態操作用オブジェクト
    private var drawerToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_urllst)
        Log.d( javaClass.simpleName, "" )
        Log.d( javaClass.simpleName, "onCreate" )
        Log.d( javaClass.simpleName, "========================" )


        // レイアウトからドロワーを探す
        //   Portrait  => ドロワーあり
        //   Landscape => ドロワーなし
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        // レイアウト中にドロワーがある場合設定を行う
        if ( drawerLayout != null ) {
            setupDrawer(drawerLayout)
        }

        if ( savedInstanceState == null ) {
            // URL一覧を表示するフラグメントを追加
            val fragmentURLLst = supportFragmentManager.findFragmentById(R.id.fragmentURLLst) as URLLstFragment
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentURLLst, URLLstFragment(), "fragmentURLLst")
                    .addToBackStack(null)
                    .commit()
        }
    }

    // アクティビティの生成が終わった後に呼ばれる
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        Log.d( javaClass.simpleName, "" )
        Log.d( javaClass.simpleName, "onPostCreate" )
        Log.d( javaClass.simpleName, "========================" )
        // ドロワーのトグルの状態を同期する
        drawerToggle?.syncState()
    }

    // 画面が回転するなど、状態が変化したときに呼ばれる
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        // 状態の変化をドロワーに伝える
        drawerToggle?.onConfigurationChanged(newConfig)
    }

    // アクションバーのアイコンがタップされると呼ばれる
    // このときドロワートグルにイベントを伝えることで
    // ナビゲーションドロワーを開閉する
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // ドロワーに伝える
        if ( drawerToggle?.onOptionsItemSelected(item) == true ) {
            return true
        }
        else {
            return super.onOptionsItemSelected(item)
        }
    }

    // ナビゲーションドロワーを開閉するためのアイコンをアクションバーに配置する
    private fun setupDrawer( drawer: DrawerLayout ) {
        val toggle = ActionBarDrawerToggle( this, drawer, R.string.app_name, R.string.app_name )
        // ドロワーのトグルを有効にする
        toggle.isDrawerIndicatorEnabled = true
        // 開いたり閉じたりのコールバックを設定する
        drawer.addDrawerListener(toggle)

        drawerToggle = toggle

        // アクションバーの設定を行う
        supportActionBar?.apply {
            // ドロワー用のアイコンを表示
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    // URLLstFragment.OnURLSelectListener
    //   URLをタップするとRSSのコンテンツ一覧を表示する画面に遷移
    override fun onSelectedURL(urlData: URLData) {
        val intent = Intent( this, RssEachActivity::class.java )
        intent.putExtra( IntentID.KEY_RSS_EACH.id, urlData )
        startActivity(intent)
    }

}
