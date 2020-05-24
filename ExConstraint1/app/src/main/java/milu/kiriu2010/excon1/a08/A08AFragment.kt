package milu.kiriu2010.excon1.a08

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import milu.kiriu2010.excon1.R
import java.io.File

// メモ入力用フラグメント
class A08AFragment: Fragment() {

    private var currentFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 端末を回転したとき、同じファイルがロードされるようにする
        if ( savedInstanceState != null && savedInstanceState.containsKey("file") ) {
            currentFile = savedInstanceState.getSerializable("file") as File
        }
    }

    // フラグメントが表示すべきビューを生成する
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_a08a, container, false)

        // メモ入力用のEditText
        val tvA08A = view.findViewById<EditText>(R.id.tvA08A)
        // 保存ボタン
        val btnA08A = view.findViewById<Button>(R.id.btnA08A)
        // 削除ボタン
        val btnA08B = view.findViewById<Button>(R.id.btnA08B)

        // 内部ストレージにメモを保存する
        //   ディレクトリ：Documents以下
        //   ファイル　　：memo-YYYY-MM-DD-hh-mm-ss
        btnA08A.setOnClickListener {
            currentFile = outputFile(currentFile, tvA08A.text.toString(),context!!)
            if ( context is OnFileOutputListener) {
                (context as OnFileOutputListener).onFileOutput()
            }
        }

        // 内部ストレージのファイルを削除
        btnA08B.setOnClickListener {
            currentFile?.delete()
            // 入力内容もクリア
            tvA08A.text.clear()
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // 端末を回転したとき、同じファイルがロードされるようにする
        currentFile?.let {
            outState.putSerializable("file",it)
        }
    }

    // 指定されたファイルで表示を更新
    fun show( file: File ) {
        // ファイルを読み込む
        val memo = inputFile(file)

        Log.d(javaClass.toString(), "=== InputFragment show ===")

        // 表示を更新する
        val tvA08A = view?.findViewById<EditText>(R.id.tvA08A) ?: return
        Log.d(javaClass.toString(), "=== InputFragment setMemo($memo) ===")
        tvA08A.setText(memo)

        currentFile = file
    }

    interface OnFileOutputListener {
        fun onFileOutput()
    }
}