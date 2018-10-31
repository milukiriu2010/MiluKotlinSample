package milu.kiriu2010.excon2.screen1.websearch

import android.app.SearchManager
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_web_search.*

// http://tekeye.uk/android/examples/web-search-example-in-android
class WebSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_search)

        ivSearchFor.setOnClickListener { _ ->
            val strSearchFor = etSearchFor.text.toString()
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra(SearchManager.QUERY, strSearchFor)
            startActivity(intent)
        }
    }

    /*
    fun onClick(view: View) {
        val strSearchFor = etSearchFor.text.toString()
        val intent = Intent(Intent.ACTION_WEB_SEARCH)
        intent.putExtra(SearchManager.QUERY, strSearchFor)
        startActivity(intent)
    }
    */
}
