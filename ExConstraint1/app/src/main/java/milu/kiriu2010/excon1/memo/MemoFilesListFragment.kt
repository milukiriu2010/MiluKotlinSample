package milu.kiriu2010.excon1.memo

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import milu.kiriu2010.file.getFiles
import milu.kiriu2010.excon1.R
import java.io.File

class MemoFilesListFragment: Fragment() {
    private lateinit var recycleView: RecyclerView

    // メモをタップしたときのコールバックインターフェース
    interface OnFileSelectListener {
        fun onFileSelected(file: File)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        recycleView = view.findViewById(R.id.filesList)
        // 縦方向に並べてリストを表示するレイアウトマネージャを設定
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycleView.layoutManager = layoutManager

        show()
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        // アクティビティがコールバックを実装していなかったら、例外を投げる
        if ( context !is OnFileSelectListener) {
            throw RuntimeException("$context must implement OnFileSelectListener")
        }
    }

    // ファイルのリストを表示する
    fun show() {
        val ctx = context ?: return
        // ファイルの一覧を表示するためのアダプター
        val adapter = MemoFilesAdapter(ctx, getFiles()) { file ->

            Log.d(javaClass.toString(), "=== MemoFilesListFragment show ===")
            // タップされたら、コールバックを呼ぶ
            (context as OnFileSelectListener).onFileSelected(file)
        }
        recycleView.adapter = adapter
    }
}