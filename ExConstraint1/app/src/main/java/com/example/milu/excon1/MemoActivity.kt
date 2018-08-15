package com.example.milu.excon1

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v4.widget.DrawerLayout
import android.util.Log
import android.view.MenuItem
import com.example.milu.intent2.R
import java.io.File

class MemoActivity : AppCompatActivity(),
        MemoFilesListFragment.OnFileSelectListener,
        InputFragment.OnFileOutputListener {
    // ナビゲーションドロワーの状態操作用オブジェクト
    private var drawerToggle: ActionBarDrawerToggle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_memo)
        if ( hasPermission() ) setViews()
    }

    // アクティビティの生成が終わった後に呼ばれる
    override fun onPostCreate( savedInstanceState: Bundle? ) {
        super.onPostCreate(savedInstanceState)
        // ドロワーのトグルの状態を同期する
        drawerToggle?.syncState()
    }

    // 画面が回転するなど、状態が変化したときに呼ばれる
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        // 状態の変化をドロワーに伝える
        drawerToggle?.onConfigurationChanged(newConfig)
    }

    // アップバーの設定を行う
    private fun setupDrawer(drawer: DrawerLayout) {
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

    // オプションメニューがタップされたときに呼ばれる
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // ドロワーに伝える
        if ( drawerToggle?.onOptionsItemSelected(item) == true ) {
            return true
        }
        else {
            return super.onOptionsItemSelected(item)
        }
    }

    // レイアウトを行うためのメソッド
    private fun setViews() {
        setContentView(R.layout.activity_memo)

        // レイアウトからドロワーを探す
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        // レイアウト中にドロワーがある場合にだけ行う処理
        if ( drawerLayout != null ) {
            setupDrawer(drawerLayout)
        }
    }

    // 外部ストレージへの書き込み権限を得てからレイアウトを行う
    private fun hasPermission(): Boolean {
        // パーミッションを持っているか確認
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            // 持っていないならパーミッションを要求
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            return false
        }

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ( !grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
            setViews()
            drawerToggle?.syncState()
        }
        else {
            finish()
        }
    }

    override fun onFileSelected(file: File) {
        Log.d(javaClass.toString(), "=== MemoActivity onFileSelected ===")
        val fragment = supportFragmentManager.findFragmentById(R.id.input) as InputFragment
        fragment.show(file)
    }

    override fun onFileOutput() {
        val fragment = supportFragmentManager.findFragmentById(R.id.list) as MemoFilesListFragment
        fragment.show()
    }
}
