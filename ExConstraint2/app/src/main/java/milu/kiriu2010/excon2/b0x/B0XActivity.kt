package milu.kiriu2010.excon2.b0x

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_b0x.*
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.b0x.bearing.BearingActivity
import milu.kiriu2010.excon2.b0x.camera.CameraActivity
import milu.kiriu2010.excon2.b0x.cameraparam.CameraParamActivity
import milu.kiriu2010.excon2.b0x.compass.CompassActivity
import milu.kiriu2010.excon2.b0x.conf.ConfActivity
import milu.kiriu2010.excon2.b0x.dragdrop.DragDropActivity
import milu.kiriu2010.excon2.b0x.gallery2.Gallery2Activity
import milu.kiriu2010.excon2.b0x.ringtone.RingtoneActivity
import milu.kiriu2010.excon2.b0x.servicelst.ServiceLstActivity

class B0XActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b0x)

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
