package milu.kiriu2010.excon2.b0x.dragdrop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_drag_drop.*
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import android.widget.Toast
import android.widget.LinearLayout
import android.view.ViewGroup
import android.graphics.PorterDuff
import android.os.Build
import androidx.annotation.RequiresApi
import android.util.Log

import milu.kiriu2010.excon2.R


// https://www.tutlane.com/tutorial/android/android-drag-and-drop-with-examples
class DragDropActivity : AppCompatActivity()
        , View.OnDragListener
        , View.OnLongClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_drop)

        //Find all views and set Tag to all draggable views
        lbl.tag = "DRAGGABLE TEXTVIEW"
        lbl.setOnLongClickListener(this)

        ingvw.setTag("ANDROID ICON")
        ingvw.setOnLongClickListener(this)

        btnDrag.setTag("DRAGGABLE BUTTON")
        btnDrag.setOnLongClickListener(this)

        //Set Drag Event Listeners for defined layouts
        layout1.setOnDragListener(this)
        layout2.setOnDragListener(this)
        layout3.setOnDragListener(this)
    }

    override fun onDrag(v: View, event: DragEvent): Boolean {
        // Defines a variable to store the action type for the incoming event
        val action = event.getAction()
        // Handles each of the expected events
        when (action) {

            DragEvent.ACTION_DRAG_STARTED -> {
                // Determines if this View can accept the dragged data
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

            DragEvent.ACTION_DRAG_ENTERED -> {
                // Applies a GRAY or any color tint to the View. Return true; the return value is ignored.
                v.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
                // Invalidate the view to force a redraw in the new tint
                v.invalidate()
                return true
            }

            DragEvent.ACTION_DRAG_LOCATION ->
                // Ignore the event
                return true

            DragEvent.ACTION_DRAG_EXITED -> {
                // Re-sets the color tint to blue. Returns true; the return value is ignored.
                // view.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                //It will clear a color filter .
                v.getBackground().clearColorFilter()
                // Invalidate the view to force a redraw in the new tint
                v.invalidate()
                return true
            }

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
                // ドラッグされているビューの親ビュー(レイアウト)
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
        val item = ClipData.Item(v.getTag() as CharSequence)
        // Create a new ClipData using the tag as a label, the plain text MIME type, and
        // the already-created item. This will create a new ClipDescription object within the
        // ClipData, and set its MIME type entry to "text/plain"
        val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
        val data = ClipData(v.getTag().toString(), mimeTypes, item)
        // Instantiates the drag shadow builder.
        val dragshadow = View.DragShadowBuilder(v)
        // Starts the drag
        v.startDragAndDrop(data        // data to be dragged
                , dragshadow   // drag shadow builder
                , v           // local data about the drag and drop operation
                , 0          // flags (not currently used, set to 0)
        )
        return true
    }

}
