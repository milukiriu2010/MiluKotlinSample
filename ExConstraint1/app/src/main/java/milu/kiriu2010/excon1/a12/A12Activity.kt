package milu.kiriu2010.excon1.a12

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import milu.kiriu2010.excon1.R
import kotlinx.android.synthetic.main.activity_a12.*

// スクロールビューの練習
class A12Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a12)
        // トップ画像を長押しするとコンテキストメニューを表示
        registerForContextMenu(ivA12)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_a12, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItemTop -> {
                ivA12.setImageResource(R.drawable.ic_a12_toppage)
                return true
            }
            R.id.menuItemLunch01 -> {
                ivA12.setImageResource(R.drawable.ic_a12_lunch01)
                return true
            }
            R.id.menuItemLunch02 -> {
                ivA12.setImageResource(R.drawable.ic_a12_dinner01)
                return true
            }
            R.id.menuItemDinner01 -> {
                ivA12.setImageResource(R.drawable.ic_a12_dinner01)
                return true
            }
            R.id.menuItemDinner02 -> {
                ivA12.setImageResource(R.drawable.ic_a12_dinner02)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    // コンテキストメニューをXMLから割り当てる
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_a12_context,menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.a12_sms -> {
                val number = "999-9999-9999"
                val uri = Uri.parse("sms:$number")
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = uri
                intent.putExtra("sms_body", "こんにちは")
                startActivity(intent)
                return true
            }
            R.id.a12_mail -> {
                val email: String = "nobody@example.com"
                val subject: String = "予約問い合わせ"
                val text: String = "以下の通り予約希望します"
                val uri = Uri.parse("mailto:")
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.apply {
                    data = uri
                    putExtra(Intent.EXTRA_EMAIL,arrayOf(email))
                    putExtra(Intent.EXTRA_SUBJECT,subject)
                    putExtra(Intent.EXTRA_TEXT,text)
                    // メールを送信するアプリをユーザがインストールしているかチェックしている
                    if ( intent.resolveActivity(packageManager) != null ){
                        startActivity(intent)
                    }
                }
                return true
            }
            R.id.share -> {
                val text: String = "おいしいレストランを紹介します"
                val intent = Intent(Intent.ACTION_SEND)
                intent.apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT,text)
                }
                val chooser = Intent.createChooser(intent, null)
                if ( intent.resolveActivity(packageManager) != null ) {
                    startActivity(chooser)
                }
                return true
            }
            R.id.browse -> {
                val url: String = "http://www.yahoo.co.jp/"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                if ( intent.resolveActivity(packageManager) != null ){
                    startActivity(intent)
                }
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}
