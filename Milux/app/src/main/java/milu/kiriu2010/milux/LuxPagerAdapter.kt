package milu.kiriu2010.milux

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter

//class LuxPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
class LuxPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    private lateinit var lux01OverViewFragment: Lux01OverViewFragment
    private lateinit var lux02OverViewFragment: Lux02OverViewFragment

    override fun getItem(pos: Int): Fragment {
        return when (pos) {
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
            else -> {
                if ( !this::lux01OverViewFragment.isInitialized ) {
                    lux01OverViewFragment = Lux01OverViewFragment()
                }
                return lux01OverViewFragment
            }
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return "Page" + position
    }
}