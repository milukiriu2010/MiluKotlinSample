package milu.kiriu2010.excon1.a11

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import milu.kiriu2010.excon1.R
import kotlinx.android.synthetic.main.activity_a11.*
import java.io.File
import java.util.*

// Android上のファイルシステムを一覧表示
class A11Activity : AppCompatActivity() {
    // 表示中のディレクトリ
    private var currentDir: File = Environment.getExternalStorageDirectory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a11)

        rvA11.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // 項目ごとに枠を描く
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        // 上だけだと、細い白い線がひかれるだけなので、黒線の定義ファイルを読み込む
        // ただし、これでも下に線が引かれるだけで、上左右がない
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this,R.drawable.divider_a11)!!)
        rvA11.addItemDecoration(dividerItemDecoration)

        // 絞り込み検索は英字とアンダーバーのみ許容
        etA11.filters = arrayOf(A11InputFilter())
        // 絞り込み検索実行
        etA11.addTextChangedListener( object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    // 入力されたフィルター文字
                    val str = it.toString()

                    if ( str.length > 0 ) {
                        val filteredLst = mutableListOf<File>()
                        currentDir.listFiles()?.sorted()?.forEach {
                            if ( it.name.toLowerCase(Locale.ROOT).contains(str) ) {
                                filteredLst.add(it)
                            }
                        }
                        showFiles(1,filteredLst)
                    }
                    else {
                        // アダプタの更新
                        showFiles()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 外部ストレージの読み込み権限を持っているかチェックする
        // 持っていない場合⇒パーミッションを要求する
        // 持っている場合  ⇒ファイル一覧を表示
        if ( hasPermission() ) showFiles()
    }

    // ファイル一覧を表示する
    private fun showFiles(flag: Int = 0, filteredLst: MutableList<File> = mutableListOf()) {

        val fileLst = when (flag) {
            0 -> currentDir.listFiles()!!.toList().sorted().toMutableList()
            else -> filteredLst
        }

        val adapter = A11Adapter(this, fileLst ) { file ->
            // ディレクトリをタップした場合、下のディレクトリのファイル一覧を表示
            if (file.isDirectory) {
                currentDir = file
                showFiles()
            }
            // ファイルをタップした場合、それぞれを処理するアクティビティへ遷移する
            else {
                //Toast.makeText(this, file.absolutePath, Toast.LENGTH_SHORT).show()

                // png : image/png
                // jpg : image/jpeg
                // mp3 : audio/mpeg
                val uri = Uri.fromFile(file)
                val ext = MimeTypeMap.getFileExtensionFromUrl(uri.toString().toLowerCase(Locale.ROOT))
                val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
                Log.d(javaClass.simpleName,"MIMETYPE={$mimeType}")

                when (mimeType) {
                    "image/png" -> startActivityImg(file)
                    "image/jpg" -> startActivityImg(file)
                    "audio/mpeg" -> startActivityAudio(file)
                    else -> startActivityTxt(file)
                }
            }
        }

        rvA11.adapter = adapter
        // アプリバーに表示中のディレクトリのパスを表示する
        title = currentDir.path
    }

    // 外部ストレージの読み込み権限を持っているかチェックし、
    // 持っていない場合、パーミッションを要求する
    private fun hasPermission(): Boolean {
        // パーミッションを持っているか確認
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {
            // 持っていない場合パーミッションを要求
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1)
            return false
        }
        return true
    }

    // パーミッションの結果を取得
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // パーミッションが許可された場合、ファイル一覧を表示する
        if ( !grantResults.isEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
            showFiles()
        }
        // パーミッションが不許可な場合、アクティビティを終了する
        else {
            finish()
        }
    }

    override fun onBackPressed() {
        if ( currentDir != Environment.getExternalStorageDirectory() ) {
            currentDir = currentDir.parentFile!!
            showFiles()
        }
        else {
            super.onBackPressed()
        }
    }

    // 画像を表示するアクティビティへ遷移
    private fun startActivityImg(file: File) {
        val intent = Intent(this,A11ImageActivity::class.java)
        intent.putExtra("file",file)
        startActivity(intent)
    }

    // オーディオを再生するアクティビティへ遷移
    private fun startActivityAudio(file: File) {
        val intent = Intent(this,A11AudioActivity::class.java)
        intent.putExtra("file",file)
        startActivity(intent)
    }

    // テキストを表示するアクティビティへ遷移
    private fun startActivityTxt(file: File) {
        val intent = Intent(this,A11TxtActivity::class.java)
        intent.putExtra("file",file)
        startActivity(intent)
    }

    // ファイルの拡張子を取得
    private fun getFileExtension(file: File): String {
        val dotPos = file.name.lastIndexOf(".")
        return when {
            ( dotPos != -1 ) && ( dotPos != 0 ) -> file.name.substring(dotPos+1)
            else -> ""
        }
    }

    // ファイルの拡張子から画像かどうか判断
    // 画像=>true
    // 画像でない=>false
    private fun isImage(file: File): Boolean {
        val ext = getFileExtension(file)

        return when (ext) {
            "jpg" -> true
            else -> false
        }
    }

    // ファイルの拡張子からオーディオかどうか判断
    // オーディオ=>true
    // オーディオでない=>false
    private fun isAudio(file: File): Boolean {
        val ext = getFileExtension(file)

        return when (ext) {
            "mp3" -> true
            else -> false
        }
    }

    // 検索テキストのフィルタ
    private class A11InputFilter: InputFilter {
        override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence {
            return if ( source.toString().matches(Regex("^[a-zA-Z_]+$") ) ) {
                source.toString()
            } else {
                ""
            }
        }
    }
}
