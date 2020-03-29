package com.harryjjacobs.musiq.ui.sections.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harryjjacobs.musiq.model.PlaylistList

class HomeViewModel : ViewModel() {

    private val pageIndex = MutableLiveData<Int>();

    fun setPageIndex(index: Int) {
        pageIndex.value = index;
    }

    fun getPageIndex() = pageIndex.value;

    private val playlistLists = MutableLiveData<PlaylistList>();
    fun getPlaylistLists() = playlistLists;
}