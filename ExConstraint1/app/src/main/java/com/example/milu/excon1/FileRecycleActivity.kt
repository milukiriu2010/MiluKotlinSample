package com.example.milu.excon1

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.example.milu.intent2.R
import kotlinx.android.synthetic.main.activity_file_recycle.*
import java.io.File

class FileRecycleActivity : AppCompatActivity() {
    // 表示中のディレクトリ
    private var currentDir: File = Environment.getExternalStorageDirectory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_recycle)

        rvFile.layoutManager = LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false)

        if ( hasPermission() ) showFiles()
    }

    private fun showFiles() {
        val adapter = FileAdapter(this, currentDir.listFiles().toList()) { file ->
            if (file.isDirectory) {
                currentDir = file
                showFiles()
            } else {
                Toast.makeText(this, file.absolutePath, Toast.LENGTH_SHORT).show()
            }
        }

        rvFile.adapter = adapter
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
            currentDir = currentDir.parentFile
            showFiles()
        }
        else {
            super.onBackPressed()
        }
    }
}
