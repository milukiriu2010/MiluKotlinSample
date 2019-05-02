package milu.kiriu2010.exdb1.opengl06

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.opengl06.w059.W059Fragment

class OpenGL06Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_gl06)

        supportFragmentManager.popBackStack()
        if (supportFragmentManager.findFragmentByTag("w058") == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, W059Fragment.newInstance(), "w058")
                    .commit()
        }
    }
}
