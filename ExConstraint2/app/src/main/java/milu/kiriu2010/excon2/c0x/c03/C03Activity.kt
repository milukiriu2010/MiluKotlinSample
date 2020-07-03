package milu.kiriu2010.excon2.c0x.c03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.databinding.ActivityC03Binding

// Data Binding
// https://qiita.com/KIRIN3qiita/items/4a2b16b4ce3c9b1cffd5
// ----------------------------------------------------------
// 簡単に書くとViewModelの内容をレイアウトファイル(activity_mail.xml等)に結び付けて、
// UIの操作をアクティビティ、やフラグメントから完全に分離できる。
// つまりMVVMができる！！
// ----------------------------------------------------------
class C03Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c03)

        // ViewModelのインスタンスを作成
        val viewModel: C03UserViewModel = ViewModelProviders.of(this).get(C03UserViewModel::class.java)

        // DataBindingのインスタンスを作成(onCreateの外でもよい)
        val binding = DataBindingUtil.setContentView<ActivityC03Binding>(this,R.layout.activity_c03)
        // ViewModelのインスタンスを設定
        binding.viewModel = viewModel
        // ライフサイクル所有者を設定
        binding.lifecycleOwner = this
    }
}