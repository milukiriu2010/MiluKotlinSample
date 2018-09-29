package milu.kiriu2010.milux.gui

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

//class LuxPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
class LuxPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    private lateinit var lux01OverViewFragment: Lux01OverViewFragment
    private lateinit var lux02OverViewFragment: Lux02OverViewFragment
    private lateinit var lux03OverViewFragment: Lux03OverViewFragment

    // ページ数
    private val pageCnt = 3

    override fun getItem(pos: Int): Fragment {
        return when (pos%pageCnt) {
            0 -> {
                if ( !this::lux01OverViewFragment.isInitialized ) {
                    lux01OverViewFragment = Lux01OverViewFragment()
                }
                return lux01OverViewFragment
            }
            1 -> {
                if ( !this::lux02OverViewFragment.isInitialized ) {
                    lux02OverViewFragment = Lux02OverViewFragment()
                }
                return lux02OverViewFragment
            }
            2 -> {
                if ( !this::lux03OverViewFragment.isInitialized ) {
                    lux03OverViewFragment = Lux03OverViewFragment()
                }
                return lux03OverViewFragment
            }
            else -> {
                if ( !this::lux01OverViewFragment.isInitialized ) {
                    lux01OverViewFragment = Lux01OverViewFragment()
                }
                return lux01OverViewFragment
            }
        }
    }

    override fun getCount(): Int = pageCnt
    //override fun getCount(): Int = Int.MAX_VALUE

    /*
    override fun getPageTitle(position: Int): CharSequence? {
        return "Page" + position
    }
    */
}