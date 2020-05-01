package milu.kiriu2010.excon2.a0x.tabbed

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabbedPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(pos: Int): Fragment {
        return when (pos) {
            0 -> CanvasFragment1.newInstance(0)
            1 -> CanvasFragment1.newInstance()
            2 -> CanvasFragment1.newInstance(2)
            3 -> CanvasFragment1.newInstance(3)
            4 -> CanvasFragment1.newInstance(4)
            5 -> CanvasFragment1.newInstance(5)
            6 -> CanvasFragment1.newInstance(6)
            7 -> CanvasFragment2.newInstance()
            else -> CanvasFragment1.newInstance()
        }
    }

    override fun getCount(): Int = 8
}