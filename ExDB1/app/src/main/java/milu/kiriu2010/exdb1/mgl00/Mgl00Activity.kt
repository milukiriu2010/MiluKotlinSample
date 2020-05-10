package milu.kiriu2010.exdb1.mgl00

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_mgl00.*
import milu.kiriu2010.exdb1.R
import milu.kiriu2010.exdb1.mgl00.nvbo.DepthCull01Fragment

// -------------------------------------------
// 立体描画
// (0) 正四面体
// (1) 立方体
// (2) 正八面体
// (3) 正十二面体
// (4) 正二十面体
// (5) 球
// (6) トーラス
// -------------------------------------------
// (1) Depth/Cull
// (2) Perspective/Frustum/Ortho
// (3) Fov/Near/Far
// VBOなし
// OpenGL ES 2.0
// -------------------------------------------
class Mgl00Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mgl00)

        // 初期表示のフラグメントを設定
        changeFragment(0)

        // アクションバーの設定を行う
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            //setHomeButtonEnabled(true)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_mgl00, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // アクションバーのアイコンがタップされると呼ばれる
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            // 前画面に戻る
            android.R.id.home -> {
                finish()
                true
            }
            // 6:トーラス
            R.id.opengl_torus01 -> {
                changeFragment(6)
                true
            }
            // 5:球
            R.id.opengl_sphere01 -> {
                changeFragment(5)
                true
            }
            // 4:正二十面体
            R.id.opengl_icosahedron01 -> {
                changeFragment(4)
                true
            }
            // 3:正十二面体
            R.id.opengl_dodecahedron01 -> {
                changeFragment(3)
                true
            }
            // 2:正八面体
            R.id.opengl_octahedron01 -> {
                changeFragment(2)
                true
            }
            // 1:立方体
            R.id.opengl_cube01 -> {
                changeFragment(1)
                true
            }
            // 0:正四面体
            R.id.opengl_tetrahedron01 -> {
                changeFragment(0)
                true
            }
            else -> return super.onOptionsItemSelected(item!!)
        }
    }

    // 表示するフラグメントを切り替える
    private fun changeFragment(mode: Int) {
        // 現在表示しているフラグメントをスタックから外す
        supportFragmentManager.popBackStack()
        // 選択したフラグメントを表示する
        if ( supportFragmentManager.findFragmentByTag("model" + mode ) == null ) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.flMGL00, DepthCull01Fragment.newInstance(mode), "model" + mode)
                    .commit()
        }
    }
}
