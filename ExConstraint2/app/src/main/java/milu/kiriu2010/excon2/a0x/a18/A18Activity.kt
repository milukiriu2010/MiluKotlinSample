package milu.kiriu2010.excon2.a0x.a18

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.animation.*
import android.widget.ArrayAdapter
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_a18.*

// 関数を使ったサイズ変更するアニメーション
class A18Activity : AppCompatActivity() {

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
        setContentView(R.layout.activity_a18)

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

        // リピート回数を設定
        npA18R.maxValue = 2
        npA18R.minValue = 0
        npA18R.value    = repeat


        // STARTボタンをクリックすると、アニメーションを開始する
        btnA18A.setOnClickListener {
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

            Log.d( javaClass.simpleName, "${spA18.selectedItem}")

            // アニメーションに使うfactorを取得
            factor = "%1$1d.%2$1d".format( npA18A.value, npA18B.value ).toFloat()

            // アニメーションを行う時間を設定
            scale.duration = 1000;
            // アニメーションの補完方法を設定
            //scale.interpolator = CycleInterpolator(0.5f)
            scale.interpolator = when (spA18.selectedItem) {
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
            scale.repeatCount = npA18R.value
            // アニメーションが終わっても元の表示に戻らないようにする
            scale.fillAfter = true
            // アニメーションのスタート
            ivA18.startAnimation(scale)
        }
    }

    // レイアウトからスクリーンサイズを取得
    // https://akira-watson.com/android/screen-size.html
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        lw = clA18.width
        lh = clA18.height
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
