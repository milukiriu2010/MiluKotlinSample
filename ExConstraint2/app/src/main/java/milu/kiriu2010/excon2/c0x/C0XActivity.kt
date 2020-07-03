package milu.kiriu2010.excon2.c0x

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_c0x.*
import milu.kiriu2010.excon2.R
import milu.kiriu2010.excon2.c0x.c01.C01Activity
import milu.kiriu2010.excon2.c0x.c02.C02Activity
import milu.kiriu2010.excon2.c0x.c03.C03Activity

class C0XActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c0x)

        // ViewModel
        btnC01.setOnClickListener {
            val intent = Intent(this, C01Activity::class.java)
            startActivity(intent)
        }

        // LiveData
        btnC02.setOnClickListener {
            val intent = Intent(this, C02Activity::class.java)
            startActivity(intent)
        }

        // DataBinding
        btnC03.setOnClickListener {
            val intent = Intent(this, C03Activity::class.java)
            startActivity(intent)
        }

    }
}