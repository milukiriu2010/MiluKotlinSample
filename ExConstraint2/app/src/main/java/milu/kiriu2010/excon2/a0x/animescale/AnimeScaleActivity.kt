package milu.kiriu2010.excon2.a0x.animescale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.animation.*
import android.widget.ArrayAdapter
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_anime_scale.*

class AnimeScaleActivity : AppCompatActivity() {

    var factor = 0.5f

    var repeat = 2

    // レイアウトサイズ
    var lw = -1
    var lh = -1

    // 画像サイズ
    var iw = 100
    var ih = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_scale)

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

        // リピート回数を設定
        numPickerRepeat.maxValue = 2
        numPickerRepeat.minValue = 0
        numPickerRepeat.value    = repeat


        // STARTボタンをクリックすると、アニメーションを開始する
        btnA02A.setOnClickListener {
            val scale = ScaleAnimation(
                    1.0f,
                    4.0f,
                    1.0f,
                    4.0f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
            )

            Log.d( javaClass.simpleName, "${spinnerInterpolator.selectedItem}")

            // アニメーションに使うfactorを取得
            factor = "%1$1d.%2$1d".format( numPickerA1.value, numPickerB1.value ).toFloat()

            // アニメーションを行う時間を設定
            scale.duration = 1000;
            // アニメーションの補完方法を設定
            //scale.interpolator = CycleInterpolator(0.5f)
            scale.interpolator = when (spinnerInterpolator.selectedItem) {
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
            // リピート回数を設定
            scale.repeatCount = numPickerRepeat.value
            // アニメーションが終わっても元の表示に戻らないようにする
            scale.fillAfter = true
            // アニメーションのスタート
            imageViewAnime.startAnimation(scale)
        }
    }

    // レイアウトからスクリーンサイズを取得
    // https://akira-watson.com/android/screen-size.html
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        lw = layoutTop.width
        lh = layoutTop.height
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
