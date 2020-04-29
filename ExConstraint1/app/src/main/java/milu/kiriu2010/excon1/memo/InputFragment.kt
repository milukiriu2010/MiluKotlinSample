package milu.kiriu2010.excon1.memo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import milu.kiriu2010.file.inputFile
import milu.kiriu2010.file.outputFile
import milu.kiriu2010.excon1.R
import java.io.File

class InputFragment: Fragment() {

    private var currentFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if ( savedInstanceState != null && savedInstanceState.containsKey("file") ) {
            currentFile = savedInstanceState.getSerializable("file") as File
        }
    }

    // フラグメントが表示すべきビューを生成する
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_input, container, false)

        // メモ入力用のEditText
        val content = view.findViewById<EditText>(R.id.content)
        // 保存ボタン
        val saveButton = view.findViewById<Button>(R.id.save)

        saveButton.setOnClickListener {
            // メモを保存する
            currentFile = outputFile( currentFile, content.text.toString() )
            if ( context is OnFileOutputListener) {
                (context as OnFileOutputListener).onFileOutput()
            }

        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
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
        val content = view?.findViewById<EditText>(R.id.content) ?: return
        Log.d(javaClass.toString(), "=== InputFragment setMemo($memo) ===")
        content.setText(memo)

        currentFile = file
    }

    interface OnFileOutputListener {
        fun onFileOutput()
    }
}