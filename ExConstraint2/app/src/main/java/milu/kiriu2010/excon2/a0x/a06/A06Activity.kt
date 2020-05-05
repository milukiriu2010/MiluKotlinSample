package milu.kiriu2010.excon2.a0x.a06

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_a06.*
import milu.kiriu2010.excon2.R

// コンテキストメニュー
// http://tekeye.uk/android/examples/ui/using-android-context-menus
class A06Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a06)

        registerForContextMenu(ivA06)
    }

    // コンテキストメニューを開く
    // Show the Context Menu When Requested
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu?.setHeaderTitle("Choose a filter");
        menuInflater.inflate(R.menu.menu_a06,menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    // 画像に選択した色でフィルターをかける
    // Act on the Selected Item
    override fun onContextItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.menu_red -> {
                ivA06.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN)
                true
            }
            R.id.menu_blue -> {
                ivA06.setColorFilter(Color.BLUE, PorterDuff.Mode.LIGHTEN)
                true
            }
            R.id.menu_green -> {
                ivA06.setColorFilter(Color.GREEN, PorterDuff.Mode.LIGHTEN)
                true
            }
            R.id.menu_clear -> {
                ivA06.setColorFilter(null)
                true
            }
            else -> {
                super.onContextItemSelected(item)
                true
            }
        }
    }

}
