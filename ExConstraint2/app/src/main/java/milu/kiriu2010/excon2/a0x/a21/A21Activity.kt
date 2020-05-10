package milu.kiriu2010.excon2.a0x.a21

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.*
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_a21.*
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.a0x.a18.HesitateInterpolator

// アニメ(移動/回転/透明)
class A21Activity : AppCompatActivity() {

    var factor = 0.5f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a21)

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }

        // 補完スピナーに補完一覧を設定
        spA21.adapter = ArrayAdapter.createFromResource(this, R.array.a21_interpolators, android.R.layout.simple_spinner_item )

        // 補完のファクターを設定
        // 数値１桁目
        npA21A.maxValue = 9
        npA21A.minValue = 0
        npA21A.value    = factor.toInt()
        // 小数点１桁目
        npA21B.maxValue = 9
        npA21B.minValue = 0
        npA21B.value    = ((factor*10)%10).toInt()

        // アニメーションに使うfactorを取得
        factor = "%1$1d.%2$1d".format( npA21A.value, npA21B.value ).toFloat()

        // ボタンをクリックすると、移動を開始する
        btnA21A_MOVE.setOnClickListener {
            ivA21.animate().apply {
                ivA21.x = 0f
                ivA21.y = 0f

                // アニメーションを行う時間を設定
                duration = 1000

                // アニメーションの補完方法を設定
                interpolator = when (spA21.selectedItem) {
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
        btnA21B_ROTATE.setOnClickListener {
            ivA21.animate().apply {
                ivA21.rotation = 0f

                // アニメーションを行う時間を設定
                duration = 1000

                // アニメーションの補完方法を設定
                interpolator = when (spA21.selectedItem) {
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
        btnA21C_ALPHA.setOnClickListener {
            ivA21.animate().apply {
                ivA21.alpha = 1.0f

                // アニメーションを行う時間を設定
                duration = 1000

                // アニメーションの補完方法を設定
                interpolator = when (spA21.selectedItem) {
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

        // ボタンをクリックすると元に戻る
        btnA21D_ORG.setOnClickListener {
            ivA21.x = 0f
            ivA21.y = 0f
            ivA21.alpha = 1f
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
