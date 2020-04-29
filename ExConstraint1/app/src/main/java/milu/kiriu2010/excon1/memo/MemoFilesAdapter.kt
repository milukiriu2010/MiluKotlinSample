package milu.kiriu2010.excon1.memo

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import milu.kiriu2010.excon1.R
import java.io.File

class MemoFilesAdapter(private val context: Context,
                        // ファイルの一覧
                       private val files: List<File>,
                       // タップ時のコールバック
                       private val onFileClicked: (File) -> Unit
                       ) : RecyclerView.Adapter<MemoFilesAdapter.FileViewHolder>() {
    private val inflater = LayoutInflater.from(context)

    class FileViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.title)
        val updatedTime = view.findViewById<TextView>(R.id.lastModified)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = inflater.inflate(R.layout.list_memo_row, parent, false )
        val viewHolder = FileViewHolder(view)

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
        // ファイル名の表示
        holder.title.text = file.name
        // 最終更新日時の表示
        holder.updatedTime.text = context.getString(R.string.last_modified,file.lastModified())
    }

}