package milu.kiriu2010.excon2.a0x.contextmenu

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import milu.kiriu2010.excon2.R
import kotlinx.android.synthetic.main.activity_context_menu.*

// http://tekeye.uk/android/examples/ui/using-android-context-menus
class ContextMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_context_menu)

        registerForContextMenu(imageView)
    }

    // Show the Context Menu When Requested
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu?.setHeaderTitle("Choose a filter");
        menuInflater.inflate(R.menu.menu_color,menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    // Act on the Selected Item
    override fun onContextItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {
            R.id.menu_red -> {
                imageView.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN)
                true
            }
            R.id.menu_blue -> {
                imageView.setColorFilter(Color.BLUE, PorterDuff.Mode.LIGHTEN)
                true
            }
            R.id.menu_green -> {
                imageView.setColorFilter(Color.GREEN, PorterDuff.Mode.LIGHTEN)
                true
            }
            R.id.menu_clear -> {
                imageView.setColorFilter(null)
                true
            }
            else -> {
                super.onContextItemSelected(item!!)
                true
            }
        }
    }

}
