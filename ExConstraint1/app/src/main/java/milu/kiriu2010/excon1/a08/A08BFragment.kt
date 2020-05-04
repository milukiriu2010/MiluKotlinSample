package milu.kiriu2010.excon1.a08

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import milu.kiriu2010.excon1.R
import java.io.File

// メモ一覧表示用フラグメント
class A08BFragment: Fragment() {
    private lateinit var recycleView: RecyclerView

    // メモをタップしたときのコールバックインターフェース
    interface OnFileSelectListener {
        fun onFileSelected(file: File)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        val view = inflater.inflate(R.layout.fragment_a08b, container, false)

        // メモ一覧を表示するリサイクラービュー
        recycleView = view.findViewById(R.id.rvA08A)
        // 縦方向に並べてリストを表示するレイアウトマネージャを設定
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycleView.layoutManager = layoutManager

        show()
        return view
    }

    override fun onAttach(context: Context) {
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
        val adapter = A08BAdapter(ctx, getFiles(ctx)) { file ->
            Log.d(javaClass.toString(), "=== A08BFragment show ===")
            // タップされたら、コールバックを呼ぶ
            (context as OnFileSelectListener).onFileSelected(file)
        }
        recycleView.adapter = adapter
    }
}