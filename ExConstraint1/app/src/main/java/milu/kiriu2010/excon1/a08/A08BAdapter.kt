package milu.kiriu2010.excon1.a08

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import milu.kiriu2010.excon1.R
import java.io.File

// メモ一覧表示用アダプタ
class A08BAdapter(private val context: Context,
                        // ファイルの一覧
                  private val files: List<File>,
                       // タップ時のコールバック
                  private val onFileClicked: (File) -> Unit
                       ) : RecyclerView.Adapter<A08BAdapter.FileViewHolder>() {
    private val inflater = LayoutInflater.from(context)

    class FileViewHolder(view: View): RecyclerView.ViewHolder(view) {
        // メモのファイル名
        val title = view.findViewById<TextView>(R.id.tvA08B)
        // メモの最終更新時刻
        val updatedTime = view.findViewById<TextView>(R.id.tvA08C)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = inflater.inflate(R.layout.adapter_a08b, parent, false )
        val viewHolder = FileViewHolder(view)

        // 一覧に表示されたメモをクリックすると、ファイルを読み込む
        view.setOnClickListener {
            //　タップされた位置に対応したメモを得る
            val file = files[viewHolder.adapterPosition]
            // コールバックを呼ぶ
            onFileClicked(file)
        }

        return viewHolder
    }

    override fun getItemCount() = files.size

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]
        // ファイル名
        holder.title.text = file.name
        // ファイルの最終更新日時
        holder.updatedTime.text = context.getString(R.string.FMT_A08,file.lastModified())
    }

}