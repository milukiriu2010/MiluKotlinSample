package milu.kiriu2010.excon2.a0x.a22

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

// ページャの各ページ
// ページごとに配置するフラグメントを変えている
class A22PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(pos: Int): Fragment {
        return when (pos) {
            0 -> A22CanvasFragment1.newInstance(0)
            1 -> A22CanvasFragment1.newInstance()
            2 -> A22CanvasFragment1.newInstance(2)
            3 -> A22CanvasFragment1.newInstance(3)
            4 -> A22CanvasFragment1.newInstance(4)
            5 -> A22CanvasFragment1.newInstance(5)
            6 -> A22CanvasFragment1.newInstance(6)
            7 -> A22CanvasFragment2.newInstance()
            else -> A22CanvasFragment1.newInstance()
        }
    }

    override fun getCount(): Int = 8
}