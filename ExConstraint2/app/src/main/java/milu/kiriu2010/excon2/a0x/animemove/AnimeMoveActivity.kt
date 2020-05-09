package milu.kiriu2010.excon2.a0x.animemove

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.*
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_anime_move.*
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.a0x.a18.HesitateInterpolator

class AnimeMoveActivity : AppCompatActivity() {

    var factor = 0.5f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_move)

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }

        // 補完スピナーに補完一覧を設定
        spA18.adapter = ArrayAdapter.createFromResource(this, R.array.a18_interpolators, android.R.layout.simple_spinner_item )

        // 補完のファクターを設定
        // 数値１桁目
        npA18A.maxValue = 9
        npA18A.minValue = 0
        npA18A.value    = factor.toInt()
        // 小数点１桁目
        npA18B.maxValue = 9
        npA18B.minValue = 0
        npA18B.value    = ((factor*10)%10).toInt()

        // アニメーションに使うfactorを取得
        factor = "%1$1d.%2$1d".format( npA18A.value, npA18B.value ).toFloat()

        // ボタンをクリックすると、移動を開始する
        btnA02A.setOnClickListener {
            ivA18.animate().apply {
                ivA18.x = 0f
                ivA18.y = 0f

                // アニメーションを行う時間を設定
                duration = 1000

                // アニメーションの補完方法を設定
                interpolator = when (spA18.selectedItem) {
                    "LinearInterpolator" -> LinearInterpolator()
                    "AccelerateInterpolator" -> AccelerateInterpolator(factor)
                    "DecelerateInterpolator" -> DecelerateInterpolator(factor)
                    "AccelerateDecelerateInterpolator" -> AccelerateDecelerateInterpolator()
                    "AnticipateInterpolator" -> AnticipateInterpolator(factor)
                    "OvershootInterpolator" -> OvershootInterpolator(factor)
                    "AnticipateOvershootInterpolator" -> AnticipateOvershootInterpolator(factor)
                    "BounceInterpolator" -> BounceInterpolator()
                    "CycleInterpolator" -> CycleInterpolator(factor)
                    "HesitateInterpolator" -> HesitateInterpolator()
                    else -> LinearInterpolator()
                }
                x(500f)
                y(500f)
            }
        }

        // ボタンをクリックすると、回転を開始する
        btnRotate.setOnClickListener {
            ivA18.animate().apply {
                ivA18.rotation = 0f

                // アニメーションを行う時間を設定
                duration = 1000

                // アニメーションの補完方法を設定
                interpolator = when (spA18.selectedItem) {
                    "LinearInterpolator" -> LinearInterpolator()
                    "AccelerateInterpolator" -> AccelerateInterpolator(factor)
                    "DecelerateInterpolator" -> DecelerateInterpolator(factor)
                    "AccelerateDecelerateInterpolator" -> AccelerateDecelerateInterpolator()
                    "AnticipateInterpolator" -> AnticipateInterpolator(factor)
                    "OvershootInterpolator" -> OvershootInterpolator(factor)
                    "AnticipateOvershootInterpolator" -> AnticipateOvershootInterpolator(factor)
                    "BounceInterpolator" -> BounceInterpolator()
                    "CycleInterpolator" -> CycleInterpolator(factor)
                    "HesitateInterpolator" -> HesitateInterpolator()
                    else -> LinearInterpolator()
                }

                // 3回転
                rotation( 360*3f )
            }
        }

        // ボタンをクリックすると、透過を開始する
        btnAlpha.setOnClickListener {
            ivA18.animate().apply {
                ivA18.alpha = 1.0f

                // アニメーションを行う時間を設定
                duration = 1000

                // アニメーションの補完方法を設定
                interpolator = when (spA18.selectedItem) {
                    "LinearInterpolator" -> LinearInterpolator()
                    "AccelerateInterpolator" -> AccelerateInterpolator(factor)
                    "DecelerateInterpolator" -> DecelerateInterpolator(factor)
                    "AccelerateDecelerateInterpolator" -> AccelerateDecelerateInterpolator()
                    "AnticipateInterpolator" -> AnticipateInterpolator(factor)
                    "OvershootInterpolator" -> OvershootInterpolator(factor)
                    "AnticipateOvershootInterpolator" -> AnticipateOvershootInterpolator(factor)
                    "BounceInterpolator" -> BounceInterpolator()
                    "CycleInterpolator" -> CycleInterpolator(factor)
                    "HesitateInterpolator" -> HesitateInterpolator()
                    else -> LinearInterpolator()
                }

                // 透明にする
                alpha( 0f )
            }
        }
    }

    // アクションバーのアイコンがタップされると呼ばれる
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            // 前画面に戻る
            android.R.id.home -> {
                finish()
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }
}
