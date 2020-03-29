package com.harryjjacobs.musiq.ui.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


object FragmentUtil {
    fun addFragmentToContainer(fm: FragmentManager, fragment: Fragment, containerId: Int, uniqueTag: String) {
        val transaction: FragmentTransaction = fm.beginTransaction();
        transaction.add(containerId, fragment, uniqueTag);
        transaction.commit();
    }
}