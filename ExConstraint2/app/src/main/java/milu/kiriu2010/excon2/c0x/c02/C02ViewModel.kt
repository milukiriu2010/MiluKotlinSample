package milu.kiriu2010.excon2.c0x.c02

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class C02ViewModel: ViewModel() {
    // 変更を監視したい値をMutableLiveData<>で宣言。
    // Int型のLiveDataを作成
    val counterB: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    // 初期値を0に設定
    init {
        counterB.value = 0
    }
}