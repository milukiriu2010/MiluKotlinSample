package milu.kiriu2010.excon2.a0x.a21

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_a18.*
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

        // 補間スピナーに補完一覧を設定
        spA21.adapter = ArrayAdapter.createFromResource(this,
                R.array.a21_interpolators,
                android.R.layout.simple_spinner_item )

        // ファクターを設定するビューを表示・非表示制御する
        spA21.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item = spA21.selectedItem
                when (item) {
                    "LinearInterpolator" -> factorVisibility(View.INVISIBLE)
                    "AccelerateInterpolator" -> factorVisibility(View.VISIBLE)
                    "DecelerateInterpolator" -> factorVisibility(View.VISIBLE)
                    "AccelerateDecelerateInterpolator" -> factorVisibility(View.INVISIBLE)
                    "AnticipateInterpolator" -> factorVisibility(View.VISIBLE)
                    "OvershootInterpolator" -> factorVisibility(View.VISIBLE)
                    "AnticipateOvershootInterpolator" -> factorVisibility(View.VISIBLE)
                    "BounceInterpolator" -> factorVisibility(View.INVISIBLE)
                    "CycleInterpolator" -> factorVisibility(View.VISIBLE)
                    "HesitateInterpolator" -> factorVisibility(View.INVISIBLE)
                }
            }
        }
        // 初期表示は、LinearInterpolatorなので、
        // ファクターを設定するビューは、非表示とする
        factorVisibility(View.INVISIBLE)

        // 補間のファクターを設定
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
            // ファクター値を更新
            refreshFactor()

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
            // ファクター値を更新
            refreshFactor()

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
            // ファクター値を更新
            refreshFactor()

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

    // ファクターを設定するビューを表示・非表示制御する
    private fun factorVisibility(visivility: Int) {
        npA21A.visibility = visivility
        tvA21.visibility = visivility
        npA21B.visibility = visivility
    }

    // ファクター値を更新
    private fun refreshFactor() {
        // アニメーションに使うfactorを取得
        factor = "%1$1d.%2$1d".format( npA21A.value, npA21B.value ).toFloat()
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
