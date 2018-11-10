package milu.kiriu2010.excon2.screen2

import android.content.Intent
import android.media.Ringtone
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main2.*
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.screen2.bearing.BearingActivity
import milu.kiriu2010.excon2.screen2.camera.CameraActivity
import milu.kiriu2010.excon2.screen2.cameraparam.CameraParamActivity
import milu.kiriu2010.excon2.screen2.compass.CompassActivity
import milu.kiriu2010.excon2.screen2.conf.ConfActivity
import milu.kiriu2010.excon2.screen2.dragdrop.DragDropActivity
import milu.kiriu2010.excon2.screen2.gallery2.Gallery2Activity
import milu.kiriu2010.excon2.screen2.ringtone.RingtoneActivity
import milu.kiriu2010.excon2.screen2.servicelst.ServiceLstActivity

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // 設定画面
        btnConf.transformationMethod = null
        btnConf.setOnClickListener {
            val intent = Intent(this,ConfActivity::class.java)
            startActivity(intent)
        }

        // ギャラリー表示
        btnGallery.transformationMethod = null
        btnGallery.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivity(intent)
        }

        // ギャラリー表示２
        btnGallery2.setOnClickListener {
            val intent = Intent(this, Gallery2Activity::class.java)
            startActivity(intent)
        }

        // カメラ起動
        btnCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        // カメラ２
        btnCameraParam.setOnClickListener {
            val intent = Intent(this, CameraParamActivity::class.java)
            startActivity(intent)
        }

        // ドラッグ＆ドロップ
        btnDragDrop.setOnClickListener {
            val intent = Intent(this, DragDropActivity::class.java)
            startActivity(intent)
        }

        // Bearing
        btnBearing.setOnClickListener {
            val intent = Intent(this, BearingActivity::class.java)
            startActivity(intent)
        }

        // コンパス
        btnCompass.setOnClickListener {
            val intent = Intent(this, CompassActivity::class.java)
            startActivity(intent)
        }

        // サービス一覧
        btnServiceLst.setOnClickListener {
            val intent = Intent(this, ServiceLstActivity::class.java)
            startActivity(intent)
        }

        // 着信音
        btnRingtone.setOnClickListener {
            val intent = Intent(this, RingtoneActivity::class.java)
            startActivity(intent)
        }

    }
}
