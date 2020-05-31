package milu.kiriu2010.excon1.a17

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import milu.kiriu2010.excon1.R
import kotlinx.android.synthetic.main.activity_a17.*
import java.util.*

// リサイクラービューの練習
class A17Activity : AppCompatActivity() {

    private val timeZones = TimeZone.getAvailableIDs().map{
        id -> TimeZone.getTimeZone(id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a17)

        rvA17.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        // アダプタを結びつける
        setAdater()

        // 絞り込み検索は英字とアンダーバーのみ許容
        etA17.filters = arrayOf( object: InputFilter {
            override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence {
                return if ( source.toString().matches(Regex("^[a-zA-Z_]+$") ) ) {
                    source.toString()
                } else {
                    ""
                }
            }
        })

        // 絞り込み検索実行
        etA17.addTextChangedListener( object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val str = it.toString()
                    if ( str.length > 0 ) {
                        val filteredTimezones = mutableListOf<TimeZone>()
                        timeZones.forEach { timeZone ->
                            if ( timeZone.id.toLowerCase(Locale.ROOT).contains(str) ) {
                                filteredTimezones.add(timeZone)
                            }
                        }

                        // アダプタを結びつける
                        setAdater(filteredTimezones)
                    }
                    else {
                        // アダプタを結びつける
                        setAdater()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // アダプタを結びつける
    private fun setAdater( filteredLst: List<TimeZone> = listOf() ) {
        val lst = if (filteredLst.size > 0) {
                filteredLst
            }
            else {
                timeZones
            }

        val adapter = A17Adapter(this,lst) { timeZone ->
            Toast.makeText(this, timeZone.displayName, Toast.LENGTH_LONG).show()
        }

        rvA17.adapter = adapter
    }

    class A17Adapter(
            context: Context,
            private val timeZones: List<TimeZone>,
            private val onItemClicked: (TimeZone)->Unit )
        : RecyclerView.Adapter<A17Adapter.A17ViewHolder>(){

        private val inflater = LayoutInflater.from(context)
        //private val timeZones = TimeZone.getAvailableIDs().map{
        //    id -> TimeZone.getTimeZone(id)
        //}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): A17ViewHolder {
            val view = inflater.inflate(R.layout.adapter_a17, parent, false)
            val viewHolder = A17ViewHolder(view)

            view.setOnClickListener{
                val position = viewHolder.adapterPosition
                val timeZone = timeZones[position]
                onItemClicked(timeZone)
            }

            return viewHolder
        }

        override fun getItemCount() = timeZones.size

        override fun onBindViewHolder(holder: A17ViewHolder, position: Int) {
            val timeZone = timeZones[position]
            holder.timeZone.text = timeZone.id
        }

        class A17ViewHolder(view: View): RecyclerView.ViewHolder(view){
            val timeZone = view.findViewById<TextView>(R.id.tvA17)
        }
    }
}
