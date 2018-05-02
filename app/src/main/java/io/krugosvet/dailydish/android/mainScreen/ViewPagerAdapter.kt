package io.krugosvet.dailydish.android.mainScreen

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import io.krugosvet.dailydish.android.utils.ViewPagerFragment
import java.util.*

class ViewPagerAdapter<F : Fragment>(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

    private val fragmentList = ArrayList<F>()

    override fun getItem(position: Int): F = fragmentList[position]

    override fun getItemPosition(`object`: Any): Int {
        val position = fragmentList.indexOf(`object`)
        return if (position == -1) PagerAdapter.POSITION_NONE else position
    }

    override fun getCount() = fragmentList.size

    fun addFragments(vararg fragment: F) {
        fragmentList.addAll(fragment)
        notifyDataSetChanged()
    }

    fun getFragment(fragmentClass: Class<F>) = fragmentList.firstOrNull { fragmentClass.isInstance(it) }

    override fun getPageTitle(position: Int): CharSequence? {
        val fragment = fragmentList[position]
        return if (fragment is ViewPagerFragment) fragment.getFragmentTitle() else ""
    }
}
