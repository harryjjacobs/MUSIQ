package com.harryjjacobs.musiq.ui.sections

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.harryjjacobs.musiq.R
import com.harryjjacobs.musiq.ui.sections.home.HomeFragment
import com.harryjjacobs.musiq.ui.sections.home.HomeViewModel
import com.harryjjacobs.musiq.ui.sections.search.SearchFragment

private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2,
        R.string.tab_text_3
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        // TODO: switch statement to choose fragment
        Log.i("SectionsPagerAdapter", position.toString());
        return when (position) {
            0 -> HomeFragment.newInstance(0)
            1 -> SearchFragment.newInstance(1)
            else -> throw IllegalArgumentException("Invalid item position for pager adapter")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 2
    }
}