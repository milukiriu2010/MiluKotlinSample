package milu.kiriu2010.excon2.a0x.a07

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import milu.kiriu2010.excon2.id.IntentID
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_a07.*

// QRコード/バーコードスキャン
class A07Activity : AppCompatActivity() {

    // 検索結果に対して、ブラウザ起動できるかどうか
    var isClickable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a07)
    }

    fun onClick(view: View) {
        Log.d(javaClass.simpleName,"Clicked:{$view.id}")
        when ( view.id ) {
            // "QRコード"ボタンをクリック
            R.id.btnA07A -> {
                val intent = Intent("com.google.zxing.client.android.SCAN")
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE")
                startActivityForResult(intent, IntentID.ID_A07A.value)
            }
            // "バーコード"ボタンをクリック
            R.id.btnA07B -> {
                val intent = Intent("com.google.zxing.client.android.SCAN")
                intent.putExtra("SCAN_MODE", "PRODUCT_MODE")
                startActivityForResult(intent, IntentID.ID_A07B.value)
            }
            // "その他"ボタンをクリック
            R.id.btnA07C -> {
                val intent = Intent("com.google.zxing.client.android.SCAN")
                intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR")
                startActivityForResult(intent, IntentID.ID_A07C.value)
            }
            // スキャン結果をブラウザで開く
            R.id.tvA07C -> {
                if (isClickable) {
                    val url = tvA07C.text.toString()
                    Log.d(javaClass.simpleName,"URL:{$url}")
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode,resultCode,intent)
        // スキャン結果を反映する
        if ( resultCode == RESULT_OK ) {
            tvA07A.setText(intent?.getStringExtra("SCAN_RESULT_FORMAT"))
            tvA07C.setText(intent?.getStringExtra("SCAN_RESULT"))
            // QRコードの結果は、クリックするとブラウザ起動する
            isClickable = when (requestCode) {
                IntentID.ID_A07A.value -> true
                else -> false
            }
        }
        // スキャンを実施しなかった場合
        else if ( resultCode == Activity.RESULT_CANCELED ) {
            tvA07A.setText(resources.getString(R.string.TV_A07A))
            tvA07C.setText(resources.getString(R.string.TV_A07C_CANCELED))
            isClickable = false
        }
    }
}
