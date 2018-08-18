package com.example.milu.excon1

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_saint_tropez.*

class SaintTropezActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saint_tropez)
        // トップ画像を長押しするとコンテキストメニューを表示
        registerForContextMenu(ivTopPage)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_saint_tropez, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menuItemTop -> {
                ivTopPage.setImageResource(R.drawable.toppage)
                return true
            }
            R.id.menuItemLunch01 -> {
                ivTopPage.setImageResource(R.drawable.lunch01)
                return true
            }
            R.id.menuItemLunch02 -> {
                ivTopPage.setImageResource(R.drawable.dinner01)
                return true
            }
            R.id.menuItemDinner01 -> {
                ivTopPage.setImageResource(R.drawable.dinner01)
                return true
            }
            R.id.menuItemDinner02 -> {
                ivTopPage.setImageResource(R.drawable.dinner02)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    // コンテキストメニューをXMLから割り当てる
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_saint_tropez_context,menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.sms -> {
                val number = "999-9999-9999"
                val uri = Uri.parse("sms:$number")
                var intent = Intent(Intent.ACTION_VIEW)
                intent.data = uri
                intent.putExtra("sms_body", "こんにちは")
                startActivity(intent)
                return true
            }
            R.id.mail -> {
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
