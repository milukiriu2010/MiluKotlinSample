package milu.kiriu2010.excon1.a08

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import milu.kiriu2010.excon1.R
import java.io.File

// メモ
class A08Activity : AppCompatActivity(),
        A08BFragment.OnFileSelectListener,
        A08AFragment.OnFileOutputListener {
    // ナビゲーションドロワーの状態操作用オブジェクト
    private var drawerToggle: ActionBarDrawerToggle? = null

    // ドロワーレイアウト
    private var drawerLayout: DrawerLayout? = null

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
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // 状態の変化をドロワーに伝える
        drawerToggle?.onConfigurationChanged(newConfig)
    }

    // アップバーの設定を行う
    private fun setupDrawer(drawer: DrawerLayout) {
        val toggle = ActionBarDrawerToggle( this, drawer, R.string.app_name, R.string.app_name)
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // ドロワーメニューを表示する際、ドロワーメニューの表示内容を更新
        onFileOutput()
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
        setContentView(R.layout.activity_a08)

        // レイアウトからドロワーを探す
        // DrawLayoutでない場合は、NULLのままにしておく
        drawerLayout = findViewById(R.id.dlA08) as? DrawerLayout

        // レイアウト中にドロワーがある場合にだけ行う処理
        drawerLayout?.let { setupDrawer(it) }
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

    // メモ入力用フラグメントに保存されたメモを開く
    override fun onFileSelected(file: File) {
        Log.d(javaClass.simpleName, "=== onFileSelected ===")
        val fragment = supportFragmentManager.findFragmentById(R.id.frgA08A) as A08AFragment
        fragment.show(file)
        // ドロワーレイアウトを閉じる
        drawerLayout?.closeDrawer(GravityCompat.START)
    }

    // メモ入力用フラグメントにて"保存"を押下 or ドロワーメニューを表示する際、
    // ドロワーレイアウトのメニューを更新する
    override fun onFileOutput() {
        Log.d(javaClass.simpleName, "=== onFileOutput ===")
        val fragment = supportFragmentManager.findFragmentById(R.id.frgA08B) as A08BFragment
        fragment.show()
    }
}
