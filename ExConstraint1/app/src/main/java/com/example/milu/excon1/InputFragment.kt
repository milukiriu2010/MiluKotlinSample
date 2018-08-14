package com.example.milu.excon1

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.milu.file.inputFile
import com.example.milu.file.outputFile
import com.example.milu.intent2.R
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
            if ( context is OnFileOutputListener ) {
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

        // 表示を更新する
        val content = view?.findViewById<EditText>(R.id.content) ?: return
        content.setText(memo)

        currentFile = file
    }

    interface OnFileOutputListener {
        fun onFileOutput()
    }
}