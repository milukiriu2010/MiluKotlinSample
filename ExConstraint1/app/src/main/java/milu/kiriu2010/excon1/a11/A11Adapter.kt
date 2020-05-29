package milu.kiriu2010.excon1.a11

import android.content.Context
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import milu.kiriu2010.excon1.R
import java.io.File

// Android上のファイルシステムを一覧表示に使うアダプタ
class A11Adapter(context: Context,
                 private val files: MutableList<File>,
                 private val onClick : (File) -> Unit)
    : RecyclerView.Adapter<A11Adapter.FileViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = inflater.inflate(R.layout.adapter_a11, parent, false)
        val viewHolder = FileViewHolder(view)

        view.setOnClickListener {
            onClick(files[viewHolder.adapterPosition])
        }

        return viewHolder
    }

    override fun getItemCount(): Int = files.size

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.fileName.text = files[position].name

        // ディレクトリのバックグランドは、薄赤にする
        val file = files[position]
        if (file.isDirectory) {
            holder.itemView.setBackgroundColor(Color.parseColor("#ffcccc"))
        }
    }

    class FileViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val fileName = view.findViewById<TextView>(R.id.tvA11)
    }
}