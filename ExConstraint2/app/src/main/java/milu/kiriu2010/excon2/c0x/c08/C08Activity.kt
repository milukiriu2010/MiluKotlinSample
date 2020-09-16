package milu.kiriu2010.excon2.c0x.c08

import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_c08.*
import milu.kiriu2010.excon2.R

// Ndefメッセージを取得
// https://seesaawiki.jp/w/moonlight_aska/d/Ndef%a5%e1%a5%c3%a5%bb%a1%bc%a5%b8%a4%f2%bc%e8%c6%c0%a4%b9%a4%eb
class C08Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c08)

        // Ndef対応カードの検出かチェック
        val action = intent.action

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            // Ndefメッセージの取得
            getNdef()
        }
    }

    // Ndefメッセージの取得
    private fun getNdef() {
        val raws = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)

        var str = ""
        raws?.forEach { raw ->
            val msg = raw as NdefMessage
            msg.records.forEach { record ->
                str += "Type: ${record.type}\n"
                str += "TNF : ${record.tnf}\n"
                record.payload.forEachIndexed { id, data ->
                    str += "Payload[%2d]:0x%02x/%c\n".format(id,data,data)
                }
            }
        }

        tvC08.text = str
    }
}