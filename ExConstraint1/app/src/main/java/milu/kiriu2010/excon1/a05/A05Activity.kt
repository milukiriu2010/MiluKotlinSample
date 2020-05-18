package milu.kiriu2010.excon1.a05

import android.app.Activity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import milu.kiriu2010.excon1.R

import kotlinx.android.synthetic.main.activity_a05.*
import kotlinx.android.synthetic.main.layout_a05.*
import org.json.JSONObject
import java.io.File

// --------------------------------------
// リソースのJSONをロードする
// --------------------------------------
// レイアウトを別ファイルにする例も付与
// --------------------------------------
class A05Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a05)
        setSupportActionBar(tbA05)

        // フローティングボタンをクリックすると、一番下にメッセージが表示される
        fabA05.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        // JSONを読み込み、内容を表示
        tvA05.text = this.loadJSON()
    }

    private fun loadJSON() : String {
        val sb = StringBuffer()
        val istream = this.resources.openRawResource(R.raw.json_a05)
        val reader = istream.bufferedReader()
        val iterator = reader.lineSequence().iterator()
        while ( iterator.hasNext() ) {
            sb.append( iterator.next() )
        }
        reader.close()
        istream.close()

        Log.v( this.javaClass.toString(), "=== JSON content ================" )
        Log.v( this.javaClass.toString(), sb.toString() )
        Log.v( this.javaClass.toString(), "=================================" )

        val objJSON = JSONObject(sb.toString())

        // jsonファイルの次の箇所を連結している
        // colors
        //   color
        val sbColor = StringBuffer()
        val colors = objJSON.getJSONArray("colors")
        for ( i in 0 until colors.length() ){
            val color = colors.getJSONObject(i)
            sbColor.append( color.getString("color") + File.separator )
        }
        return sbColor.toString()
    }
}
