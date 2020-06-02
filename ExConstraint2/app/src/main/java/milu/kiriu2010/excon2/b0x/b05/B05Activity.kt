package milu.kiriu2010.excon2.b0x.b05

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_b05.*
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.widget.Toast
import android.widget.LinearLayout
import android.view.ViewGroup
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import android.util.Log

import milu.kiriu2010.excon2.R

// ドラッグ＆ドロップ
// https://www.tutlane.com/tutorial/android/android-drag-and-drop-with-examples
class B05Activity : AppCompatActivity()
        , View.OnDragListener
        , View.OnLongClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b05)

        // Find all views and set Tag to all draggable views
        // ドラッグしているビューを検索するにあたり、タグを用いるため、設定する
        tvB05.tag = "DRAGGABLE TEXTVIEW"
        tvB05.setOnLongClickListener(this)

        ivB05.setTag("ANDROID ICON")
        ivB05.setOnLongClickListener(this)

        btnB05A.setTag("DRAGGABLE BUTTON")
        btnB05A.setOnLongClickListener(this)

        // Set Drag Event Listeners for defined layouts
        llB05A.setOnDragListener(this)
        llB05B.setOnDragListener(this)
        llB05C.setOnDragListener(this)
    }

    override fun onDrag(v: View, event: DragEvent): Boolean {
        // Defines a variable to store the action type for the incoming event
        val action = event.getAction()
        // Handles each of the expected events
        when (action) {
            // ドラッグ開始
            DragEvent.ACTION_DRAG_STARTED -> {
                // Determines if this View can accept the dragged data
                // MimeTypeがtext/plainのものだけをドラッグ可能としている？
                return if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    // if you want to apply color when drag started to your view you can uncomment below lines
                    // to give any color tint to the View to indicate that it can accept data.
                    // v.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                    // Invalidate the view to force a redraw in the new tint
                    //  v.invalidate();
                    // returns true to indicate that the View can accept the dragged data.
                    true
                } else false
                // Returns false. During the current drag and drop operation, this View will
                // not receive events again until ACTION_DRAG_ENDED is sent.
            }
            // ドラッグしているビューが入ってきたとき、
            // 背景を灰色にする
            DragEvent.ACTION_DRAG_ENTERED -> {
                // Applies a GRAY or any color tint to the View. Return true; the return value is ignored.
                // 29が出るまで待つ
                // https://stackoverflow.com/questions/56716093/setcolorfilter-is-deprecated-on-api29/56717316
                v.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
                // Invalidate the view to force a redraw in the new tint
                v.invalidate()
                return true
            }

            DragEvent.ACTION_DRAG_LOCATION ->
                // Ignore the event
                return true
            // ドラッグしていたビューが、出て行ったとき、
            // 背景を元の色に戻す
            DragEvent.ACTION_DRAG_EXITED -> {
                // Re-sets the color tint to blue. Returns true; the return value is ignored.
                // view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                //It will clear a color filter .
                v.getBackground().clearColorFilter()
                // Invalidate the view to force a redraw in the new tint
                v.invalidate()
                return true
            }
            // ドラッグしていたビューがドロップされたら、
            // ・ドラッグしていビューのテキストをトーストする(だが、みえない)
            // ・ドロップ先のビューの背景を元に戻す
            // ・ドロップ元のビューから、ドラッグしていたビューを削除する
            // ・ドラッグ先のビューの一番最後に加える
            DragEvent.ACTION_DROP -> {
                // Gets the item containing the dragged data
                val item = event.getClipData().getItemAt(0)
                // Gets the text data from the item.
                val dragData = item.text.toString()
                // Displays a message containing the dragged data.
                Toast.makeText(this, "Dragged data is $dragData", Toast.LENGTH_SHORT).show()
                // Turns off any color tints
                v.getBackground().clearColorFilter()
                // Invalidates the view to force a redraw
                v.invalidate()

                // ドラッグされているビュー
                val vw = event.getLocalState() as View
                // ドラッグされているビューの元の親ビュー(レイアウト)
                val owner = vw.parent as ViewGroup
                owner.removeView(vw) //remove the dragged view
                //caste the view into LinearLayout as our drag acceptable layout is LinearLayout
                // ドラッグ先のレイアウト
                val container = v as LinearLayout
                container.addView(vw)//Add the dragged view
                vw.visibility = View.VISIBLE//finally set Visibility to VISIBLE
                // Returns true. DragEvent.getResult() will return true.
                return true
            }
            // ドラッグしていたビューがドロップされたら、
            // ・ドロップ先のビューの背景を元に戻す
            // ・ドロップ結果をトーストする(こっちは、みえる)
            DragEvent.ACTION_DRAG_ENDED -> {
                // Turns off any color tinting
                v.getBackground().clearColorFilter()
                // Invalidates the view to force a redraw
                v.invalidate()
                // Does a getResult(), and displays what happened.
                if (event.getResult())
                    Toast.makeText(this, "The drop was handled.", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_SHORT).show()
                // returns true; the value is ignored.
                return true
            }
            // An unknown action type was received.
            else -> Log.e("DragDrop Example", "Unknown action type received by OnDragListener.")
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onLongClick(v: View): Boolean {
        // Create a new ClipData.Item from the ImageView object's tag
        // タグから ClipData.Item を生成
        val item = ClipData.Item(v.getTag() as CharSequence)
        // Create a new ClipData using the tag as a label, the plain text MIME type, and
        // the already-created item. This will create a new ClipDescription object within the
        // ClipData, and set its MIME type entry to "text/plain"
        // ----------------------------------------------------------------------------------
        // タグをラベルとして ClipData を生成
        // MimeType に text/plain を指定する
        val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
        val data = ClipData(v.getTag().toString(), mimeTypes, item)
        // Instantiates the drag shadow builder.
        val dragshadow = View.DragShadowBuilder(v)
        // Starts the drag
        v.startDragAndDrop(data     // data to be dragged
                , dragshadow        // drag shadow builder
                , v                 // local data about the drag and drop operation
                , 0           // flags (not currently used, set to 0)
        )
        return true
    }
}

/*
// 29以上でないと、ダメっぽいので保留
// https://stackoverflow.com/questions/56716093/setcolorfilter-is-deprecated-on-api29/56717316
fun Drawable.setColorFilter(color: Int, mode: PorterDuff.Mode = PorterDuff.Mode.SRC_ATOP ) {
    colorFilter = BlendModeColorFilter(color, mode)
}
 */
