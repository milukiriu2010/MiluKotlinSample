package milu.kiriu2010.excon2.c0x.c02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_c02.*
import milu.kiriu2010.excon2.R

// LiveData
// https://qiita.com/KIRIN3qiita/items/6f5c467a8abc7b89cbe7
// ---------------------------------------------------------------
// 簡単に書くとViewModelクラス内のデータをアクティビティやフラグメントが監視して、
// 更新を受け取れるようにしたもの。
// ---------------------------------------------------------------
// オブザーバーを設置し、ViewModelの値は更新を受け取ったらViewを更新する
// ---------------------------------------------------------------
class C02Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c02)

        // アクティビティに属するカウンター
        var counterA = 0

        // インスタンスを作成
        val viewModel = ViewModelProviders.of(this).get(C02ViewModel::class.java)

        datC02_ACTIVITY.setText(counterA.toString())
        datC02_VIEWMODEL.setText(viewModel.counterB.value.toString())

        btnC02_COUNTUP.setOnClickListener {
            counterA++
            datC02_ACTIVITY.setText(counterA.toString())

            // ViewModelの監視対象のメンバ変数を+1
            // 値を取り出す場合は[value]
            viewModel.counterB.value = viewModel.counterB.value!! + 1
        }

        // UI更新用のオブザーバー（監視役）を作成
        val countObserver = Observer<Int> { counter ->
            // 更新を受け取ったらTextViewを更新
            datC02_VIEWMODEL.setText(counter.toString())
        }

        // LiveDataを監視し、このアクティビティをLifecycleOwnerおよびオブザーバーとして渡す
        viewModel.counterB.observe(this,countObserver)
    }
}