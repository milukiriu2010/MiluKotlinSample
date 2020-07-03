package milu.kiriu2010.excon2.c0x.c03

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class C03UserViewModel: ViewModel() {

    // LiveViewを作成
    val user: MutableLiveData<C03User> by lazy {
        MutableLiveData<C03User>()
    }

    private val users = mutableListOf<C03User>(
            C03User("SUZUKI",45),
            C03User("OHTANI",25)
        )

    private var pos = 0

    // 初期値を設定
    init {
        user.value = users[pos]
    }

    // ボタンクリック時の値を変更する関数
    fun change() {
        pos = (pos+1)%2
        user.value = users[pos]
    }

    // LiveDataの更新を他のLiveDataの更新に依存させる
    val logo: LiveData<LogoMark> = Transformations.map(user) {
        when {
            user.value!!.age > 40 -> LogoMark.LOGO1
            else -> LogoMark.LOGO2
        }
    }

    enum class LogoMark {
        LOGO1,
        LOGO2
    }
}