package milu.kiriu2010.excon2.c0x.c09

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.databinding.ActivityC09Binding

// https://qiita.com/tkmd35/items/516283b4e43fd5ff7a7c
// データバインディング双方向通信
class C09Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "双方向DataBinding"

        //setContentView(R.layout.activity_c09)
        // ViewModelとバインディングする
        val binding: ActivityC09Binding = DataBindingUtil.setContentView(this, R.layout.activity_c09)
        binding.viewModel = C09ViewModel()
    }
}