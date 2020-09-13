package milu.kiriu2010.excon2.c0x.c07

import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_c07.*
import milu.kiriu2010.excon2.R
import kotlin.experimental.and

// NFでカードのIDを取得する
// https://seesaawiki.jp/w/moonlight_aska/d/NFC%a4%c7%a5%ab%a1%bc%a5%c9%a4%ceID%a4%f2%bc%e8%c6%c0%a4%b9%a4%eb
// ---------------------------------
// カードのNFC対応規格を取得する
// https://seesaawiki.jp/w/moonlight_aska/d/%a5%ab%a1%bc%a5%c9%a4%ceNFC%c2%d0%b1%fe%b5%ac%b3%ca%a4%f2%bc%e8%c6%c0%a4%b9%a4%eb
class C07Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c07)

        // ICカードの検出かチェック
        val action = intent.action

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            // NFCからID情報取得
            getId()
            // NFC対応規格を取得
            getTechList()
        }
    }

    // NFCからID情報取得
    private fun getId() {
        // NFCからID情報取得
        val ids = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID) ?: ByteArray(0)

        // ID:ラベル
        val tagId = "ID(${ids.size}bytes)"
        lblC07_ID.text = tagId

        // ID:データ
        var tagDat = ""
        ids.forEach {
            tagDat = tagDat + "%02x".format(it and 0xff.toByte())
        }
        datC07_ID.setText( tagDat )
    }

    // NFC対応規格を取得
    private fun getTechList() {
        // NFC対応情報の取得
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return

        var str = ""
        tag.techList.forEach {
            str += it + "\n"
        }
        datC07_Tech.setText(str)
    }
}