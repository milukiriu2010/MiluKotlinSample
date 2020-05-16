package milu.kiriu2010.exdb1.realm01

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter

// Realm01のリストビューに対応するアダプタ
// スケジューラの日付とタイトルを表示する
class Realm01Adapter(data: OrderedRealmCollection<Schedule>?) : RealmBaseAdapter<Schedule>(data) {

    inner class ViewHolder(cell:View) {
        val date = cell.findViewById<TextView>(android.R.id.text1)
        val title = cell.findViewById<TextView>(android.R.id.text2)
    }

    // --------------------------------------------------------------------------
    // リストビューのセルのデータが必要になると呼び出される
    // --------------------------------------------------------------------------
    //   position: セルの位置
    //   convertView: すでに作成済のセルを表すビューを受け取る。nullの場合もある
    //   parent: 親のリストビュー
    // --------------------------------------------------------------------------
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        when (convertView) {
            null -> {
                val inflater = LayoutInflater.from(parent?.context)
                view = inflater.inflate(android.R.layout.simple_expandable_list_item_2, parent, false)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
            }
            else -> {
                view = convertView
                viewHolder = view.tag as ViewHolder
            }
        }

        adapterData?.run {
            val schedule = get(position)
            viewHolder.date.text = DateFormat.format("yyyy/MM/dd",schedule.date)
            viewHolder.title.text = schedule.title
        }

        return view
    }
}