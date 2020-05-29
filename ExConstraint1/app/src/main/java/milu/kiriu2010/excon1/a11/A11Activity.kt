package milu.kiriu2010.excon1.a11

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import milu.kiriu2010.excon1.R
import kotlinx.android.synthetic.main.activity_a11.*
import java.io.File

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

        if ( hasPermission() ) showFiles()
    }

    private fun showFiles() {
        val adapter = A11Adapter(this, currentDir.listFiles()!!.toList().sorted()) { file ->
            // ディレクトリをタップした場合、下のディレクトリのファイル一覧を表示
            if (file.isDirectory) {
                currentDir = file
                showFiles()
            }
            // ファイルをタップした場合、トーストする
            else {
                Toast.makeText(this, file.absolutePath, Toast.LENGTH_SHORT).show()
            }
        }

        rvA11.adapter = adapter
        // アプリバーに表示中のディレクトリのパスを表示する
        title = currentDir.path
    }

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
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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
}
