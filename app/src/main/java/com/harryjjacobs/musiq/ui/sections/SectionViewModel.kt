package com.harryjjacobs.musiq.ui.sections

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class SectionViewModel: ViewModel() {
    private val pageIndex = MutableLiveData<Int>();

    fun setPageIndex(index: Int) {
        pageIndex.value = index;
    }

    fun getPageIndex() = pageIndex.value;
}