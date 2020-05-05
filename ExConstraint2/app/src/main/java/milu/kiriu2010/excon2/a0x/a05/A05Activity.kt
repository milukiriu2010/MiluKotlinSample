package milu.kiriu2010.excon2.a0x.a05

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_a05.*

// Web検索
// http://tekeye.uk/android/examples/web-search-example-in-android
class A05Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a05)

        // 画像をクリックすると、ブラウザを起動して、入力した内容の検索を行う
        ivA05.setOnClickListener { _ ->
            // 検索する内容
            val strSearchFor = tvA05B.text.toString()
            // 検索結果をブラウザに表示
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra(SearchManager.QUERY, strSearchFor)
            startActivity(intent)
        }
    }
}
