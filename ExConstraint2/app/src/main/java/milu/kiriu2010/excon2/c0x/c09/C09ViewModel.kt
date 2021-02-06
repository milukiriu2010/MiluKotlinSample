package milu.kiriu2010.excon2.c0x.c09

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class C09ViewModel: BaseObservable() {
    private var clickText: String = "ボタンクリックでここに表示"

    // フォーム入力内容のgetterとsetter
    @get: Bindable
    var formText: String = ""
        set(value) {
            // fieldはformTextのこと
            // フォーム入力内容がformTextにセットされる
            field = value
            // View側にrealTimeTextの変更を通知(getRealTimeText())を呼ぶ
            notifyPropertyChanged(BR.realTimeText)
            // View側にbuttonEnableの変更を通知(isButtonEnable()を呼ぶ
            notifyPropertyChanged(BR.buttonEnable)
        }

    // ボタンクリック時に表示するテキスト(TextView)のgetter
    @Bindable fun getClickText(): String {
        return clickText
    }

    // フォーム入力内容をフォーム下のTextViewに反映する getter
    @Bindable fun getRealTimeText(): String {
        return formText
    }

    // フォーム(EditText)へのテキスト入力有無で、
    // ボタン活性・非活性を制御するフラグのgetter
    @Bindable fun isButtonEnable(): Boolean {
        // 入力あり:true 入力なし:false
        return !formText.isBlank()
    }

    // ボタンクリックイベント
    fun onButtonClick() {
        // clickTextにフォーム入力テキストをセット
        clickText = formText
        // formTextを初期化
        formText = ""
        // 変更を通知
        // この記述でgetClickText()が呼ばれる
        notifyPropertyChanged(BR.clickText)
        // この記述でgetFormatText()が呼ばれる
        notifyPropertyChanged(BR.formText)
    }
}