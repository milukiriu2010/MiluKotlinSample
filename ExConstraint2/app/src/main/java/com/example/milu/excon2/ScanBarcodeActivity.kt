package com.example.milu.excon2

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_scan_barcode.*

class ScanBarcodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_barcode)
    }

    fun onClick(view: View) {
        val intent = Intent("com.google.zxing.client.android.SCAN")
        when ( view.id ) {
            R.id.btnQR    -> intent.putExtra("SCAN_MODE", "QR_CODE_MODE")
            R.id.btnProd  -> intent.putExtra("SCAN_MODE", "PRODUCT_MODE")
            R.id.btnOther -> intent.putExtra("SCAN_FORMATS", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR")
        }
        startActivityForResult(intent,IntentID2.ID_SCAN_BARCODE_REQUEST.value)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent? ) {
        if ( requestCode == IntentID2.ID_SCAN_BARCODE_REQUEST.value ) {
            if ( resultCode == RESULT_OK ) {
                tvStatus.setText(intent?.getStringExtra("SCAN_RESULT_FORMAT"))
                tvResult.setText(intent?.getStringExtra("SCAN_RESULT"))
            }
            else if ( resultCode == Activity.RESULT_CANCELED ) {
                tvStatus.setText("Press a button to start a scan.")
                tvResult.setText("Scan cancelled.")
            }
        }
    }
}
