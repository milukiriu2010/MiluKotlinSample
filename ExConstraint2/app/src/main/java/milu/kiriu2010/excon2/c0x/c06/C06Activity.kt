package milu.kiriu2010.excon2.c0x.c06

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_c06.*
import milu.kiriu2010.excon2.R
import kotlin.experimental.and

// NFCの有効/無効チェック
// https://seesaawiki.jp/w/moonlight_aska/d/NFC%a4%ce%cd%ad%b8%fa/%cc%b5%b8%fa%a4%f2%b3%ce%c7%a7%a4%b9%a4%eb
// ID取得
// https://seesaawiki.jp/w/moonlight_aska/d/NFC%a4%c7%a5%ab%a1%bc%a5%c9%a4%ceID%a4%f2%bc%e8%c6%c0%a4%b9%a4%eb
// http://www.dcom-web.co.jp/lab/mobile/android/nfc_tutorial1
// https://www.atmarkit.co.jp/ait/articles/1211/27/news072.html
class C06Activity : AppCompatActivity() {
    private lateinit var mNfcAdapter: NfcAdapter
    private lateinit var pendingIntent: PendingIntent
    private var intentFilters = arrayListOf<IntentFilter>()
    private var techLists = arrayListOf<Array<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c06)

        // NFCアダプタのインスタンス
        mNfcAdapter = NfcAdapter.getDefaultAdapter(applicationContext)
        val nfcONOFF = if (mNfcAdapter.isEnabled) "ON" else "OFF"

        // NFの有効/無効
        tvC06_ONOFF.text = nfcONOFF

        val intent2 = Intent(this,javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        pendingIntent = PendingIntent.getActivity(this,0,intent2,0)

        // 受け取るIntentを指定
        intentFilters.add(IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED))

        // 反応するタグの種類を指定
        val techs = arrayOf(
                android.nfc.tech.Ndef::class.java.name,
                android.nfc.tech.NdefFormatable::class.java.name)
        techLists.add(techs)

        btnC06_GET.setOnClickListener {
            setID1()
        }
    }

    override fun onResume() {
        super.onResume()

        // NFCタグの検出を有効化
        mNfcAdapter.enableForegroundDispatch(
                this,
                pendingIntent,
                intentFilters.toTypedArray(),
                techLists.toTypedArray())
        Log.d(javaClass.simpleName,"onResume")
    }

    override fun onPause() {
        super.onPause()

        mNfcAdapter.disableForegroundDispatch(this)
        Log.d(javaClass.simpleName,"onPause")
    }

    // NFCタグの検出時に呼ばれる
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d(javaClass.simpleName,"onNewIntent")

        setID2(intent)
    }

    private fun setID1() {
        // ICカードの検出かチェック
        val action = intent.action
        Log.d(javaClass.simpleName,"action[$action]")
        tvC06_ID.text =
                if ( NfcAdapter.ACTION_TECH_DISCOVERED.equals(action) ) {
                    // NFCからID情報取得
                    val ids = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
                    val tagID = StringBuffer("ID(${ids!!.size}bytes):")
                    ids.forEach {
                        tagID.append("%02x".format(it and 0xff.toByte()))
                    }
                    tagID.toString()
                }
                else {
                    "NO ID"
                }
    }

    private fun setID2(intent: Intent) {
        // タグのIDを取得
        val tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID) ?: return

        val list = arrayListOf<String>()
        for ( byte in tagId ) {
            list.add("%02X".format(byte.toInt() and 0xFF))
        }

        Log.d(javaClass.simpleName,"onNewIntent(${list.size})")

        // 画面に表示
        tvC06_ID.text = list.joinToString { ":" }
    }
}
