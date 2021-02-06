package milu.kiriu2010.excon2.c0x.c01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_c01.*
import milu.kiriu2010.excon2.R

// View Model
// https://qiita.com/KIRIN3qiita/items/7d833e2c010c0b2c02d9
// ----------------------------------------------------------
// アクティビティにデータを保持していると、
// 画面を回転させたときにデータが初期化されてしまったり、
// フラグメントへのデータの受け渡しが面倒だったり、
// アクティビティやフラグメントのソースが肥大化したりします。
// そこで、アクティビティやフラグメントが死んでも、データを保持し続け、
// データ管理を一元できるViewModelがあると便利というわけです。
// ----------------------------------------------------------
// ボタンをクリックしたら、
// ActivityのカウンターとViewModelのカウンターをプラス１する
// ----------------------------------------------------------

class C01Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c01)

        // 比較用のアクティビティに属するカウンター
        var counterA = 0

        // インスタンスを作成
        val viewModel = ViewModelProviders.of(this).get(C01ViewModel::class.java)

        datC01_ACTIVITY.setText(counterA.toString())
        datC01_VIEWMODEL.setText(viewModel.counterB.toString())

        btnC01_COUNTUP.setOnClickListener {
            counterA++
            viewModel.counterB++

            datC01_ACTIVITY.setText(counterA.toString())
            datC01_VIEWMODEL.setText(viewModel.counterB.toString())
        }
    }
}
