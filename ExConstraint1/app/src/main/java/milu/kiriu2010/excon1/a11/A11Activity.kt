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

        if ( hasPermission() ) showFiles()
    }

    private fun showFiles() {
        val adapter = A11Adapter(this, currentDir.listFiles()!!.toList()) { file ->
            if (file.isDirectory) {
                currentDir = file
                showFiles()
            } else {
                Toast.makeText(this, file.absolutePath, Toast.LENGTH_SHORT).show()
            }
        }

        rvA11.adapter = adapter
        // アプリバーに表示中のディレクトリのパスを表示する
        title = currentDir.path
    }

    private fun hasPermission(): Boolean {
        // パーミッションを持っているか確認
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            // 持っていない場合パーミッションを要求
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ( !grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
            showFiles()
        }
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