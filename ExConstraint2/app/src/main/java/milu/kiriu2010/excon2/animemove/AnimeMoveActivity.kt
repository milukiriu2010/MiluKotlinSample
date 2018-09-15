package milu.kiriu2010.excon2.animemove

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.*
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_anime_move.*
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.animescale.HesitateInterpolator

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
        spinnerInterpolator.adapter = ArrayAdapter.createFromResource(this, R.array.interpolators, android.R.layout.simple_spinner_item )

        // 補完のファクターを設定
        // 数値１桁目
        numPickerA1.maxValue = 9
        numPickerA1.minValue = 0
        numPickerA1.value    = factor.toInt()
        // 小数点１桁目
        numPickerB1.maxValue = 9
        numPickerB1.minValue = 0
        numPickerB1.value    = ((factor*10)%10).toInt()

        // アニメーションに使うfactorを取得
        factor = "%1$1d.%2$1d".format( numPickerA1.value, numPickerB1.value ).toFloat()

        // ボタンをクリックすると、移動を開始する
        btnStart.setOnClickListener {
            imageViewAnime.animate().apply {
                imageViewAnime.x = 0f
                imageViewAnime.y = 0f

                // アニメーションを行う時間を設定
                duration = 1000

                // アニメーションの補完方法を設定
                interpolator = when (spinnerInterpolator.selectedItem) {
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
            imageViewAnime.animate().apply {
                imageViewAnime.rotation = 0f

                // アニメーションを行う時間を設定
                duration = 1000

                // アニメーションの補完方法を設定
                interpolator = when (spinnerInterpolator.selectedItem) {
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
            imageViewAnime.animate().apply {
                imageViewAnime.alpha = 1.0f

                // アニメーションを行う時間を設定
                duration = 1000

                // アニメーションの補完方法を設定
                interpolator = when (spinnerInterpolator.selectedItem) {
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
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
