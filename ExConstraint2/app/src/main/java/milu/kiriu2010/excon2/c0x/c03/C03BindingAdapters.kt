package milu.kiriu2010.excon2.c0x.c03

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import milu.kiriu2010.excon2.R

@BindingAdapter("app:logoIcon")
fun logIcon(view: ImageView, logo: C03UserViewModel.LogoMark) {
    when (logo) {
        C03UserViewModel.LogoMark.LOGO1 -> {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.c03_1_miraitowa))
        }
        else -> {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.c03_2_someity))
        }
    }
}