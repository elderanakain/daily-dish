package io.krugosvet.dailydish.android.mainScreen

import androidx.fragment.app.*
import androidx.viewpager.widget.*
import io.krugosvet.dailydish.android.utils.*
import java.util.*

class ViewPagerAdapter<F : Fragment>(manager: FragmentManager) :
    FragmentStatePagerAdapter(manager) {

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

  override fun getPageTitle(position: Int): CharSequence? {
    val fragment = fragmentList[position]
    return if (fragment is ViewPagerFragment) fragment.getFragmentTitle() else ""
  }
}
