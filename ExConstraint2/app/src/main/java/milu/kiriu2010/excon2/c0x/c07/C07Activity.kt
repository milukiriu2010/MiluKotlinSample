package milu.kiriu2010.excon2.c0x.c07

import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_c07.*
import milu.kiriu2010.excon2.R
import kotlin.experimental.and

// NFでカードのIDを取得する
// https://seesaawiki.jp/w/moonlight_aska/d/NFC%a4%c7%a5%ab%a1%bc%a5%c9%a4%ceID%a4%f2%bc%e8%c6%c0%a4%b9%a4%eb
class C07Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c07)

        // ICカードの検出かチェック
        val action = intent.action

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            // NFCからID情報取得
            val ids = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID) ?: return
            var tagId = "ID(${ids.size}bytes)："
            ids.forEach {
                tagId = tagId + "%02x".format(it and 0xff.toByte())
            }
            tvC07.text = tagId
        }
    }
}